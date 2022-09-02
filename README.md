# pruebadmartinez

# comandos para generar imagen docker

---Generar Imagen Docker
docker build -t dmartinezl.prueba:v1

--Crear la red
docker network create --driver nat prueba-servicios

--Ejecutar Aplicacion dentro del contenedor
docker run -p 8060:8060 --name dmartinezl.prueba --network prueba-servicios dmartinezl.prueba:v1

# Jar ubicacion
\dmartinezl.prueba\target

# url base 
http://localhost:8080/h2-ui/

# Nota 
Se utilizo una base h2 en memoria por este motivo no se adjuntan scripts
