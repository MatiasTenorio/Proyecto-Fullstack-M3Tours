$microservicios = @(
    "empresas",
    "usuarios",
    "operadores",
    "tour",
    "reserva",
    "pago",
    "detallepago",
    "destinos",
    "categoria",
    "itinerarios"
)

# NUEVO: Lista de microservicios cuyos errores no detendrán la cola
$ignorarErroresEn = @(
    "operadores"
)

$procesosExitosos = @()

foreach ($ms in $microservicios) {
    $ruta = Join-Path $PSScriptRoot $ms
    if (Test-Path $ruta) {
        Write-Host "Lanzando microservicio: $ms..." -ForegroundColor Cyan
        
        $logFile = Join-Path $ruta "startup.log"
        if (Test-Path $logFile) { Remove-Item $logFile -Force }

        $comando = "cd '$ruta'; ./mvnw spring-boot:run 2>&1 | Tee-Object -FilePath 'startup.log'"
        $proceso = Start-Process powershell -ArgumentList "-NoExit", "-Command", $comando -PassThru
        
        $arrancoCorrectamente = $false
        $fallo = $false
        $tiempoMaximo = 60
        $cronometro = [System.Diagnostics.Stopwatch]::StartNew()

        while ($cronometro.Elapsed.TotalSeconds -lt $tiempoMaximo) {
            if (Test-Path $logFile) {
                $logContent = Get-Content $logFile -ErrorAction SilentlyContinue | Out-String
                
                if ($logContent -match "Started .* in .* seconds" -or $logContent -match "Tomcat started on port") {
                    $arrancoCorrectamente = $true
                    break
                }
                
                if ($logContent -match "APPLICATION FAILED TO START" -or $logContent -match "BUILD FAILURE") {
                    $fallo = $true
                    break
                }
            }
            Start-Sleep -Seconds 2
        }
        $cronometro.Stop()

        # Evaluar qué pasó
        if ($fallo -or (-not $arrancoCorrectamente)) {
            
            # NUEVO: Verificamos si el microservicio está en la lista de ignorados
            if ($ms -in $ignorarErroresEn) {
                Write-Host "¡Error en '$ms'! Pero está en la lista de ignorados. Continuando con la cola..." -ForegroundColor Magenta
                
                # Lo agregamos a la lista de "exitosos" no porque lo sea, sino para que 
                # si un proceso posterior SÍ falla (y cancela la cola), este también se 
                # cierre automáticamente y no te deje ventanas basura abiertas.
                $procesosExitosos += $proceso
            } 
            else {
                # Lógica de cancelación original para errores NO ignorados
                Write-Host "¡Error crítico detectado al iniciar '$ms'!" -ForegroundColor Red
                Write-Host "Cerrando los microservicios lanzados anteriormente para liberar RAM..." -ForegroundColor Yellow
                
                foreach ($p in $procesosExitosos) {
                    if (-not $p.HasExited) {
                        Write-Host "Deteniendo PID $($p.Id)..." -ForegroundColor DarkGray
                        cmd.exe /c "taskkill /PID $($p.Id) /T /F >nul 2>&1"
                    }
                }
                
                Write-Host "--------------------------------------------------------" -ForegroundColor Red
                Write-Host "Cola cancelada. La ventana de '$ms' se ha quedado abierta para depuración." -ForegroundColor Red
                break 
            }
        } else {
            Write-Host "-> '$ms' inició correctamente." -ForegroundColor Green
            $procesosExitosos += $proceso
        }
    } else {
        Write-Host "La ruta $ruta no existe. Saltando..." -ForegroundColor DarkGray
    }
}