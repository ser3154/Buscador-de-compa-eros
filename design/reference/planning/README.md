# Calendario y solicitudes de participación

Pantallas implementadas en Kotlin con Jetpack Compose a partir del diseño del proyecto:

- Calendario, 17 de octubre.
- Solicitudes pendientes.

## Navegación

Entrar → Mis planes → Solicitudes para participar. La flecha superior permite regresar.

`CalendarScreen.kt` permite cambiar de mes y seleccionar fechas. Los planes de ejemplo están en octubre de 2026. `ParticipationRequestsScreen.kt` permite aceptar, rechazar con confirmación y deshacer decisiones; actualiza el contador de participantes. El estado de las decisiones se mantiene en `AppNavigation.kt` al navegar.

## Integración pendiente

Los datos son locales de demostración; no existe conexión con un servidor. «Ver actividad» muestra un resumen en un diálogo hasta conectar la pantalla de detalle del equipo. Mensajes y Perfil muestran un aviso mientras se integran esas rutas. Se adaptó el regreso de solicitudes al calendario para poder recorrer ambas pantallas.

Los SVG y PNG originales se conservan como referencia del diseño. La barra inferior usa ahora el componente compartido CoincideNavigationBar con los iconos Material del inicio. Las pantallas usan la fuente sans serif del sistema.


## Tema y barra compartida

La barra inferior se define una sola vez en AppNavigation para Explorar, Calendario, Solicitudes y Crear actividad. Conserva el diseño del inicio: cuatro iconos, etiquetas y un indicador redondeado. Las pantallas de acceso no muestran esta barra.

Theme.kt selecciona automáticamente la paleta clara u oscura con isSystemInDarkTheme(). Color.kt y PlanningColors consultan colores semánticos del tema para fondos, superficies, bordes, texto y botones. La paleta mantiene el acento turquesa de la aplicación.
