import { useEffect, useState } from "react"

const STORAGE_KEY = "theme"
const MEDIA_QUERY = "(prefers-color-scheme: dark)"

function getStoredPreference() {
  const stored = window.localStorage.getItem(STORAGE_KEY)
  // Sin valor guardado (o uno inválido) significa "automático": seguir al SO.
  return stored === "light" || stored === "dark" ? stored : "system"
}

/**
 * Preferencia de tema con tres valores: "light" | "dark" | "system".
 * - "light"/"dark": se guardan explícitamente y se aplican tal cual.
 * - "system": no se guarda nada (mismo criterio que el script anti-parpadeo
 *   de index.html) y `resolvedTheme` sigue en vivo los cambios de
 *   `prefers-color-scheme` del sistema operativo.
 */
export function useTheme() {
  const [preference, setPreference] = useState(getStoredPreference)
  const [systemPrefersDark, setSystemPrefersDark] = useState(
    () => window.matchMedia(MEDIA_QUERY).matches
  )

  // Suscripción al tema del sistema operativo.
  useEffect(() => {
    const media = window.matchMedia(MEDIA_QUERY)
    function handleChange(event) {
      setSystemPrefersDark(event.matches)
    }
    media.addEventListener("change", handleChange)
    return () => media.removeEventListener("change", handleChange)
  }, [])

  const resolvedTheme =
    preference === "system" ? (systemPrefersDark ? "dark" : "light") : preference

  // Sincroniza la clase `dark` del <html> con el tema ya resuelto.
  useEffect(() => {
    document.documentElement.classList.toggle("dark", resolvedTheme === "dark")
  }, [resolvedTheme])

  // Persiste (o limpia) la preferencia explícita del usuario.
  useEffect(() => {
    if (preference === "system") {
      window.localStorage.removeItem(STORAGE_KEY)
    } else {
      window.localStorage.setItem(STORAGE_KEY, preference)
    }
  }, [preference])

  return { preference, resolvedTheme, setPreference }
}
