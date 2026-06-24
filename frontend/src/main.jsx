import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'


createRoot(document.getElementById('root')).render(
  <StrictMode>
      <div className="container-fluid vh-100">
          <div className="row h-100">

              {/* Columna izquierda - Login */}
              <div className="col-6">
                  <h1 className="border-l-black">Sistema de Gestión para monitoreo de la marcha</h1>
              </div>

              {/* Columna derecha - Animación */}
              <div className="col-6">
                  <h1>Animación</h1>

              </div>

          </div>
      </div>
  </StrictMode>,
)


