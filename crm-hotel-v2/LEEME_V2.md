# Hotel 3 Estrellas · Versión 2

## Qué cambió

- **Cliente sin cuenta:** elige tipo de habitación, fechas y número de personas; ve qué habitaciones están disponibles y cuáles no; elige el número y confirma. Nunca ve IDs.
- **Roles:** `ADMIN` (todo) y `RECEPCIONISTA` (reservaciones y clientes; no puede borrar). Entrada del personal en `/login.html`.
- **Panel del personal** en `/admin.html`: reservaciones, habitaciones, tipos y precios, reseñas, clientes y usuarios.
- **4 tipos de habitación y 12 habitaciones:** Estándar (1–3, $200), Superior (4–6, $350), Junior Suite (7–9, $450), Familiar (10–12, $550).
- **Opiniones con estrellas:** 1 = Mala, 5 = Buena, 2 a 4 = Regular.
- **Ubicación:** mapa de Google Maps, botón "Ver en Google Maps" y WhatsApp/llamada al 968 112 8923.
- **Contraseñas cifradas** (BCrypt). Se cifran solas la primera vez que arranca la aplicación.
- **Espacios para fotos** en `src/main/resources/static/img/` (ver `img/LEEME.txt`).

## Pasos para instalarla (en este orden)

1. **Detén la aplicación** en IntelliJ.
2. **Respalda la base:** Workbench → Server → Data Export → selecciona `hotel_crm` → Start Export.
3. **Ejecuta `sql/01_migracion_v2.sql`** en MySQL Workbench. Al final muestra 12 habitaciones y 4 tipos. Si algún tipo no aparece, avísame antes de seguir.
4. **Reemplaza tu carpeta del proyecto** por esta (o copia `src/` y `pom.xml`).
5. **Recarga Maven** (ícono de elefante en IntelliJ) para que descargue Spring Security.
6. **Ejecuta la aplicación** y abre:
   - Sitio público: http://localhost:8080
   - Acceso del personal: http://localhost:8080/login.html

Usuarios de prueba (ya existentes en la base):

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | admin123 | ADMIN |
| recepcionista | recep123 | RECEPCIONISTA |

## Supuestos a confirmar

- **Familiar:** el máximo de personas es **6** (tu texto decía "tres o 6").
- **Servicios que no mencionaste:** TV y "amueblada" en Superior, Junior Suite y Familiar quedaron en **Sí**; clima en Estándar y Superior quedó en **No**.
- **Tipo de cama:** Superior y Junior Suite = "Cama matrimonial"; Estándar = "2 camas individuales"; Familiar = "Camas adicionales y literas".
- **Tamaño (m²):** solo tiene dato la Estándar (15 a 25 m²). Los demás no lo muestran.
- **WhatsApp:** el enlace usa `52` + número. Si no abre el chat, cambia `529681128923` por `521681128923`.
- **Pagos:** la reservación crea un pago PENDIENTE. No hay cobro con tarjeta en línea (sin pasarela, según el alcance del documento).

## Archivos

- **Nuevos:** `config/SecurityConfig`, `config/DataInitializer`, `models/Resena`, `repositories/ResenaRepository`, `services/ResenaService`, `services/UsuarioDetailsService`, `controllers/ResenaController`, `controllers/SesionController`, `dto/*` (3 archivos), `static/login.html`, `static/admin.html`, `static/css/estilos.css`, `static/img/LEEME.txt`, `sql/01_migracion_v2.sql`.
- **Reescritos:** `models/TipoHabitacion`, `models/Habitacion`, `models/Usuario`, `models/Reservacion`, `repositories/*` (7 archivos), `services/ReservacionService`, `services/HabitacionService`, `services/TipoHabitacionService`, `services/UsuarioService`, `controllers/ReservacionController`, `controllers/HabitacionController`, `static/index.html`, `pom.xml` (solo se agregó Spring Security).
- **Sin cambios:** `ClienteService`, `ClienteController`, `PagoService`, `PagoController`, `ServicioService`, `ServicioController`, `DetalleReservacionService`, `DetalleReservacionController`, `TipoHabitacionController`, `UsuarioController`, `CrmHotelApplication`, `application.properties`.
