import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { autenticarUsuario } from '../services/restService';

import loginSideImg from '../assets/login_side.jpg';

const Login: React.FC = () => {
    const [usuario, setUsuario] = useState<string>('');
    const [clave, setClave] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const res = await autenticarUsuario(usuario, clave);
            if (res.success && res.rol) {
                if (res.rol.toLowerCase() === 'gerente') {
                    localStorage.setItem('user', JSON.stringify(res));
                    navigate('/dashboard');
                } else {
                    setError(`Acceso restringido: El rol '${res.rol}' no tiene permisos de Gerente.`);
                }
            } else {
                setError(res.message || 'Credenciales inválidas');
            }
        } catch (err) {
            setError('Error de conexión con el servidor.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex h-screen w-full bg-white font-['Segoe_UI'] overflow-hidden">
            {/* PANEL IZQUIERDO: FORMULARIO */}
            <div className="w-full lg:w-1/2 flex flex-col items-center justify-center p-4">
                <div className="w-[350px] flex flex-col items-center">
                    {/* Títulos */}
                    <h1 className="text-[32px] font-bold text-[#0066CC] leading-tight mb-1">
                        Eureka Bank
                    </h1>
                    <p className="text-[16px] text-[#666666] mb-[50px]">
                        Acceso al Sistema de Cajeros (RESTful)
                    </p>

                    <form className="w-full flex flex-col items-center" onSubmit={handleSubmit}>
                        {error && (
                            <div className="w-[320px] mb-4 p-3 bg-red-50 text-red-600 text-xs font-bold rounded-lg border border-red-100 text-center">
                                {error}
                            </div>
                        )}

                        {/* Usuario */}
                        <div className="w-[320px] mb-6">
                            <label className="block text-[14px] font-bold text-[#333333] mb-2 text-center">
                                Usuario
                            </label>
                            <input
                                type="text"
                                required
                                className="w-full h-[45px] px-4 border border-gray-300 rounded-[22px] focus:outline-none focus:border-[#0066CC] text-[15px] text-[#333333] transition-all"
                                placeholder="Nombre de usuario"
                                value={usuario}
                                onChange={(e) => setUsuario(e.target.value)}
                                disabled={loading}
                            />
                        </div>

                        {/* Contraseña */}
                        <div className="w-[320px] mb-[45px]">
                            <label className="block text-[14px] font-bold text-[#333333] mb-2 text-center">
                                Contraseña
                            </label>
                            <input
                                type="password"
                                required
                                className="w-full h-[45px] px-4 border border-gray-300 rounded-[22px] focus:outline-none focus:border-[#0066CC] text-[15px] text-[#333333] transition-all"
                                placeholder="••••••••"
                                value={clave}
                                onChange={(e) => setClave(e.target.value)}
                                disabled={loading}
                            />
                        </div>

                        {/* Botón */}
                        <button
                            type="submit"
                            disabled={loading}
                            className="w-[320px] h-[50px] bg-[#0066CC] text-white text-[16px] font-bold rounded-[25px] hover:bg-[#004C99] transition-colors cursor-pointer active:scale-[0.98] disabled:opacity-50"
                        >
                            {loading ? 'CARGANDO...' : 'INICIAR SESIÓN'}
                        </button>
                    </form>
                </div>
            </div>

            {/* PANEL DERECHO: IMAGEN CORPORATIVA */}
            <div className="hidden lg:block lg:w-1/2 relative">
                <img 
                    src={loginSideImg} 
                    alt="Eureka Bank Login Side" 
                    className="absolute inset-0 w-full h-full object-cover"
                />
                <div className="absolute inset-0 bg-[#0066CC]/20"></div>
            </div>
        </div>
    );
};

export default Login;
