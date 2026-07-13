$microservicios = @(
    "empresas",
    "usuarios",
    "operadores",
    "tours",
    "reservas",
    "pagos",
    "detallepago",
    "destinos",
    "categorias",
    "itinerarios"
)

foreach ($ms in $microservicios) {
    $ruta = Join-Path $PSScriptRoot $ms
    if (Test-Path $ruta) {
        Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$ruta'; ./mvnw spring-boot:run"
        Start-Sleep -Seconds 7
    }
}