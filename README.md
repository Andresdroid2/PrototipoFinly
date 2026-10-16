# FINLY

Es un sistema organizador de gastos; este es un prototipo de aplicación Android para registrar gastos personales de forma rápida y llevar un control claro de en qué se va el dinero.

---

## Características del prototipo

- Registro de gastos con monto, categoría y fecha.
- Widget de registro rápido con el que se ingresar monto, elige categoría y confirmar, sin abrir la app. (Primero debe iniciar sesión en la app)
- Listado de gastos registrados.
- La app mantiene los registros de los datos de forma local en el dispositivo.

## Tecnologías

| Componente | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose |
| Persistencia local | Room |
| Widget | Jetpack Glance |
| IDE | Android Studio |
| Build | Gradle (Kotlin DSL) |

## Requisitos

- Android Studio
- JDK 17
- Dispositivo o emulador con Android 8.0 (API 26) o superior

> Ajusta la versión mínima de Android según el `minSdk` de tu `build.gradle.kts`.

## Instalación y ejecución

```bash
# 1. Clonar el repositorio
git clone https://github.com/Andresdroid2/PrototipoFinly.git
# 2. Abrir la carpeta en Android Studio
# 3. Esperar a que Gradle sincronice las dependencias
# 4. Ejecutar en un emulador o dispositivo (botón Run ▶)
```

## Cómo usar el widget

1. Mantén presionada una zona libre de la pantalla de inicio.
2. Abre **Widgets** y busca **FINLY**.
3. Arrastra el widget a la pantalla.
4. Inicia sesión del usuario en la app.
5. Ingresa el monto, elige la categoría y toca **+**.

