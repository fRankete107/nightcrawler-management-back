@echo off
echo Iniciando deploy a produccion...

:: Compilar con Maven
echo Compilando proyecto...
call ./mvnw clean package -DskipTests

:: Verificar si la compilación fue exitosa
if errorlevel 1 (
    echo Error en la compilacion
    pause
    exit /b 1
)

:: Configurar variables para SSH
set SSH_USER=ubuntu
set SSH_HOST=ec2-184-169-131-60.us-west-1.compute.amazonaws.com
set SSH_KEY=../../../aws/webber-usa.pem
set REMOTE_PATH=/home/ubuntu/nightcrawler-management/

:: Copiar el archivo WAR al servidor
echo Copiando archivo WAR al servidor...
scp -i %SSH_KEY% target/management-0.0.1-SNAPSHOT.war %SSH_USER%@%SSH_HOST%:%REMOTE_PATH%

:: Reiniciar el servicio en el servidor
echo Reiniciando servicio...
ssh -i %SSH_KEY% %SSH_USER%@%SSH_HOST% "sudo systemctl restart nightcrawler-management"

echo Deploy completado!
pause 