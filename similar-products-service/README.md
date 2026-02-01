# similar-products-service

Spring Boot application implementing a REST API for returning similar products, as part of a backend technical test.

## Descripción

Este servicio es una aplicación REST construida con **Spring Boot 4.0.2** y **Java 17** que proporciona un endpoint para obtener productos similares a un producto dado. El servicio interactúa con APIs externas para recuperar información de productos similares y devuelve los resultados en formato JSON.

## Requisitos

- **Java 17** o superior
- **Maven 3.6** o superior
- **Spring Boot 4.0.2**

## Tecnologías Utilizadas

- **Spring Boot** 4.0.2 - Framework web
- **Spring Web MVC** - Para la creación de APIs REST
- **Apache HttpClient5** - Para realizar peticiones HTTP a servicios externos
- **Spring DevTools** - Para desarrollo ágil
- **JUnit y Spring Test** - Para pruebas unitarias

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/jromana/dev/similar/products/service/
│   │   ├── SimilarProductsServiceApplication.java    # Clase principal
│   │   ├── config/
│   │   │   ├── AppConfig.java                        # Configuración general
│   │   │   └── AsyncConfig.java                      # Configuración asíncrona
│   │   ├── controller/
│   │   │   └── SimilarProductController.java         # Controlador REST
│   │   ├── dto/
│   │   │   └── ProductDTO.java                       # DTO de Producto
│   │   └── service/
│   │       ├── SimilarProductService.java            # Interfaz del servicio
│   │       └── SimilarProductServiceImpl.java         # Implementación
│   └── resources/
│       └── application.properties                     # Configuración
└── test/
    └── java/...                                       # Tests unitarios
```

## API Endpoints

### GET `/product/{productId}/similar`

Obtiene una lista de productos similares para un producto específico.

**Parámetros:**
- `productId` (path parameter): Identificador único del producto

**Respuesta:**
```json
[
  {
    "id": "1",
    "name": "Producto Similar 1",
    "description": "Descripción del producto"
  },
  {
    "id": "2",
    "name": "Producto Similar 2",
    "description": "Descripción del producto"
  }
]
```

**Códigos de Respuesta:**
- `200 OK` - Productos similares obtenidos exitosamente
- `4xx` - Errores del cliente
- `5xx` - Errores del servidor

## Instalación y Ejecución

### 1. Requisitos previos

Asegúrate de tener instalado:
- Java 17+
- Maven 3.6+

### 2. Compilar el proyecto

```bash
mvn clean install
```

### 3. Ejecutar la aplicación

#### Opción A: Con Maven
```bash
mvn spring-boot:run
```

#### Opción B: Con el JAR compilado
```bash
mvn clean package
java -jar target/similar-products-service-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en `http://localhost:8080` (puerto por defecto)

## Pruebas

### Ejecutar todos los tests

```bash
mvn test
```

### Ejecutar un test específico

```bash
mvn test -Dtest=NombreDelTest
```

## Desarrollo

### DevTools

El proyecto incluye **Spring DevTools** para facilitar el desarrollo. Los cambios en el código se recargan automáticamente.

### Estructura de Componentes

- **Controller**: Maneja las peticiones HTTP
- **Service**: Lógica de negocio para obtener productos similares
- **DTO**: Objetos de transferencia de datos
- **Config**: Configuración de Spring (async, beans, etc.)

## Licencia

Este proyecto está bajo la licencia especificada en el archivo [LICENSE](LICENSE).

## Autor

**Javier Romana López**

## Notas Adicionales

- El servicio utiliza Apache HttpClient5 para realizar llamadas HTTP
- Soporta operaciones asíncronas
- Revisa `AppConfig.java` y `AsyncConfig.java` para ajustar la configuración según tus necesidades
