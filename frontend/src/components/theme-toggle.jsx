import { Moon, Sun, Monitor } from "lucide-react"
import { cn } from "@/lib/utils"
import { useTheme } from "@/hooks/use-theme"

const OPCIONES = [
  { value: "light", label: "Modo claro", Icon: Sun },
  { value: "dark", label: "Modo oscuro", Icon: Moon },
  { value: "system", label: "Automático (según el dispositivo)", Icon: Monitor },
]

export function ThemeToggle() {
  const { preference, setPreference } = useTheme()

  return (
    <div
      role="radiogroup"
      aria-label="Tema de la interfaz"
      className="inline-flex items-center gap-0.5 rounded-full border border-border bg-card p-1 shadow-sm"
    >
      {OPCIONES.map((opcion) => {
        const activo = preference === opcion.value
        return (
          <button
            key={opcion.value}
            type="button"
            role="radio"
            aria-checked={activo}
            aria-label={opcion.label}
            title={opcion.label}
            onClick={() => setPreference(opcion.value)}
            className={cn(
              "inline-flex size-7 items-center justify-center rounded-full transition-colors focus-visible:ring-3 focus-visible:ring-ring/50 focus-visible:outline-none",
              activo
                ? "bg-primary text-primary-foreground"
                : "text-muted-foreground hover:text-foreground"
            )}
          >
            <opcion.Icon className="size-3.5" />
          </button>
        )
      })}
    </div>
  )
}
