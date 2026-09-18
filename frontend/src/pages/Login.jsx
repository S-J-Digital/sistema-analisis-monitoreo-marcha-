import { useState } from "react"
import { Eye, EyeOff } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Checkbox } from "@/components/ui/checkbox"
import { ThemeToggle } from "@/components/theme-toggle"
import { SensorTrace } from "@/components/sensor-trace"

const PUNTOS_MARCA = [
  "Sesiones de captura centralizadas",
  "Clasificación automática de patologías",
  "Una experiencia distinta según tu rol",
]

export default function Login() {
  const [usuario, setUsuario] = useState("")
  const [contrasena, setContrasena] = useState("")
  const [mostrarContrasena, setMostrarContrasena] = useState(false)
  const [recordar, setRecordar] = useState(false)
  const [cargando, setCargando] = useState(false)
  const [error, setError] = useState("")

  async function handleSubmit(event) {
    event.preventDefault()
    setError("")
    setCargando(true)
    try {
      // TODO(auth): sustituir por src/services/auth.js -> POST {gateway}/api/login/
      // y guardar el JWT devuelto antes de redirigir según el rol.
      await new Promise((resolve) => setTimeout(resolve, 700))
      console.log("Intento de inicio de sesión", { usuario, recordar })
    } catch {
      setError("No se pudo iniciar sesión. Verifica tu usuario y contraseña.")
    } finally {
      setCargando(false)
    }
  }

  return (
    <main className="relative flex min-h-svh items-center justify-center bg-secondary px-4 py-10 sm:px-6">
      <div className="absolute top-4 right-4">
        <ThemeToggle />
      </div>

      <div className="grid w-full max-w-4xl overflow-hidden rounded-2xl bg-card shadow-xl shadow-black/5 ring-1 ring-border md:grid-cols-[minmax(280px,380px)_1fr]">
        <BrandPanel />

        <div className="flex flex-col justify-center px-8 py-12 sm:px-12">
          <span className="text-xs font-semibold tracking-wider text-primary uppercase">
            Bienvenido de nuevo
          </span>
          <h1 className="mt-2 font-heading text-[1.75rem] font-semibold text-foreground">
            Iniciar sesión
          </h1>
          <p className="mt-1 text-sm text-muted-foreground">
            Introduce tus credenciales para continuar.
          </p>

          <form className="mt-8 flex flex-col gap-5" onSubmit={handleSubmit} noValidate>
            <div className="flex flex-col gap-2">
              <Label htmlFor="login-usuario">Usuario</Label>
              <Input
                id="login-usuario"
                name="usuario"
                type="text"
                autoComplete="username"
                placeholder="nombre@clinica.com"
                value={usuario}
                onChange={(event) => setUsuario(event.target.value)}
                className="h-11 rounded-xl px-3.5 text-[15px]"
                required
              />
            </div>

            <div className="flex flex-col gap-2">
              <Label htmlFor="login-contrasena">Contraseña</Label>
              <div className="relative">
                <Input
                  id="login-contrasena"
                  name="contrasena"
                  type={mostrarContrasena ? "text" : "password"}
                  autoComplete="current-password"
                  placeholder="Tu contraseña"
                  value={contrasena}
                  onChange={(event) => setContrasena(event.target.value)}
                  className="h-11 rounded-xl px-3.5 pr-11 text-[15px]"
                  required
                />
                <button
                  type="button"
                  onClick={() => setMostrarContrasena((valor) => !valor)}
                  aria-label={mostrarContrasena ? "Ocultar contraseña" : "Mostrar contraseña"}
                  className="absolute inset-y-0 right-1 my-auto flex size-9 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:text-foreground focus-visible:ring-3 focus-visible:ring-ring/50 focus-visible:outline-none"
                >
                  {mostrarContrasena ? <EyeOff className="size-4" /> : <Eye className="size-4" />}
                </button>
              </div>
            </div>

            <div className="flex items-center justify-between gap-4 text-sm">
              <div className="flex items-center gap-2">
                <Checkbox
                  id="login-recordar"
                  checked={recordar}
                  onCheckedChange={(valor) => setRecordar(valor === true)}
                />
                <Label htmlFor="login-recordar" className="font-normal text-muted-foreground">
                  Recordarme
                </Label>
              </div>
              <a href="#" className="font-medium text-primary hover:underline">
                ¿Olvidaste tu contraseña?
              </a>
            </div>

            {error && (
              <p role="alert" className="text-sm text-destructive">
                {error}
              </p>
            )}

            <Button type="submit" disabled={cargando} className="h-11 rounded-xl text-[15px]">
              {cargando ? "Entrando…" : "Entrar"}
            </Button>
          </form>

          <p className="mt-6 text-sm text-muted-foreground">
            ¿No tienes acceso?{" "}
            <a href="#" className="font-medium text-primary hover:underline">
              Contacta a tu clínica
            </a>
            .
          </p>
        </div>
      </div>
    </main>
  )
}

function BrandPanel() {
  return (
    <aside className="relative hidden flex-col justify-center gap-8 overflow-hidden bg-[radial-gradient(120%_140%_at_15%_10%,var(--brand-ink-2)_0%,var(--brand-ink)_65%)] px-10 py-12 text-brand-cream md:flex">
      <div>
        <SensorTrace className="max-w-full opacity-90" />
        <LiveCaptureBadge />
      </div>

      <div>
        <h2 className="font-heading text-[2rem] font-medium tracking-tight">
          Marcha<span className="text-brand-gold">IA</span>
        </h2>
        <p className="mt-3 max-w-[30ch] text-sm leading-relaxed text-brand-cream/70">
          Monitoreo de marcha basado en sensores IMU, para el equipo clínico y para quienes cuidan.
        </p>
      </div>

      <ul className="flex flex-col gap-2.5 text-sm text-brand-cream/85">
        {PUNTOS_MARCA.map((punto) => (
          <li key={punto} className="flex items-center gap-2.5">
            <span className="size-1.5 shrink-0 rounded-full bg-brand-gold" aria-hidden="true" />
            {punto}
          </li>
        ))}
      </ul>
    </aside>
  )
}

function LiveCaptureBadge() {
  return (
    <div className="mt-2 flex items-center gap-2 text-xs font-medium tracking-wide text-brand-cream/55">
      <span className="relative flex size-2" aria-hidden="true">
        <span className="absolute inline-flex size-full animate-ping rounded-full bg-emerald-400 opacity-75" />
        <span className="relative inline-flex size-2 rounded-full bg-emerald-400" />
      </span>
      Captura de marcha en curso
    </div>
  )
}
