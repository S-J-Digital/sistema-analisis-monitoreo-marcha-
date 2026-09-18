import { useEffect, useRef } from "react"

const ANCHO = 300
const ALTO = 116
const MUESTRAS = 120
const RITMO_MS = 55 // ~18 muestras/seg — se siente a ritmo de sensor, no de video
const AMORTIGUACION = 0.9
const PROB_PASO = 0.03 // probabilidad, por muestra, de un pico tipo "paso"

/** Genera un valor -1..1 por caminata aleatoria con "pasos" ocasionales más marcados. */
function crearGenerador() {
  let valor = 0
  let velocidad = 0
  return function siguiente() {
    velocidad += (Math.random() - 0.5) * 0.06
    if (Math.random() < PROB_PASO) {
      velocidad += (Math.random() < 0.5 ? -1 : 1) * (0.55 + Math.random() * 0.35)
    }
    velocidad *= AMORTIGUACION
    valor = Math.max(-1, Math.min(1, valor + velocidad))
    return valor
  }
}

/**
 * Traza de sensor en vivo: valores aleatorios (con "pasos" ocasionales) que
 * se desplazan de forma continua, como la lectura de un acelerómetro IMU.
 * Se dibuja en <canvas> — más barato que animar cientos de nodos SVG por
 * cuadro. Respeta `prefers-reduced-motion` mostrando una lectura fija.
 */
export function SensorTrace({ className }) {
  const canvasRef = useRef(null)

  useEffect(() => {
    const canvas = canvasRef.current
    if (!canvas) return undefined

    const ctx = canvas.getContext("2d")
    const dpr = Math.min(window.devicePixelRatio || 1, 2)
    canvas.width = ANCHO * dpr
    canvas.height = ALTO * dpr
    ctx.scale(dpr, dpr)

    const color =
      getComputedStyle(canvas).getPropertyValue("--brand-gold").trim() || "#d9c08f"
    const colorGuia =
      getComputedStyle(canvas).getPropertyValue("--brand-cream").trim() || "#f4efe3"

    const siguienteMuestra = crearGenerador()
    const muestras = Array.from({ length: MUESTRAS }, () => 0)
    const paso = ANCHO / (MUESTRAS - 1)
    const medio = ALTO / 2
    const amplitud = ALTO / 2 - 10

    function dibujarGuias() {
      ctx.strokeStyle = colorGuia
      ctx.globalAlpha = 0.1
      ctx.lineWidth = 1
      ;[0.2, 0.5, 0.8].forEach((fraccion) => {
        const y = ALTO * fraccion
        ctx.beginPath()
        ctx.moveTo(0, y)
        ctx.lineTo(ANCHO, y)
        ctx.stroke()
      })
      ctx.globalAlpha = 1
    }

    function dibujar(progreso) {
      ctx.clearRect(0, 0, ANCHO, ALTO)
      dibujarGuias()

      ctx.beginPath()
      muestras.forEach((valor, indice) => {
        const x = indice * paso - progreso * paso
        const y = medio - valor * amplitud
        if (indice === 0) ctx.moveTo(x, y)
        else ctx.lineTo(x, y)
      })
      ctx.strokeStyle = color
      ctx.lineWidth = 2
      ctx.lineJoin = "round"
      ctx.lineCap = "round"
      ctx.shadowColor = color
      ctx.shadowBlur = 6
      ctx.stroke()

      const xLider = ANCHO - progreso * paso
      const yLider = medio - muestras[muestras.length - 1] * amplitud
      ctx.beginPath()
      ctx.fillStyle = color
      ctx.shadowBlur = 9
      ctx.arc(xLider, yLider, 3, 0, Math.PI * 2)
      ctx.fill()
      ctx.shadowBlur = 0
    }

    const prefiereMenosMovimiento = window.matchMedia(
      "(prefers-reduced-motion: reduce)"
    ).matches

    if (prefiereMenosMovimiento) {
      for (let i = 0; i < MUESTRAS; i += 1) muestras[i] = siguienteMuestra()
      dibujar(0)
      return undefined
    }

    let ultimaMuestraEn = performance.now()
    let cuadro = requestAnimationFrame(function avanzar(ahora) {
      if (ahora - ultimaMuestraEn >= RITMO_MS) {
        muestras.shift()
        muestras.push(siguienteMuestra())
        ultimaMuestraEn = ahora
      }
      dibujar(Math.min(1, (ahora - ultimaMuestraEn) / RITMO_MS))
      cuadro = requestAnimationFrame(avanzar)
    })

    return () => cancelAnimationFrame(cuadro)
  }, [])

  return (
    <canvas
      ref={canvasRef}
      role="img"
      aria-label="Lectura en vivo del sensor de marcha"
      style={{ width: ANCHO, height: ALTO }}
      className={className}
    />
  )
}
