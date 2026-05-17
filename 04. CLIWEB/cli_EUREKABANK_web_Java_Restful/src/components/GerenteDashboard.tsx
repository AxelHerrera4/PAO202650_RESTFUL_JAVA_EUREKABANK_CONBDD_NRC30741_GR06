import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { LogOut, RefreshCcw, Landmark, Users, CreditCard, Activity } from 'lucide-react';
import { listarCuentasPorSucursal } from '../services/restService';

interface UserInfo {
    nombre: string;
    rol: string;
    idSucursal: number;
    nombreSucursal: string;
}

interface CuentaInfo {
    numero: string;
    titular: string;
    saldo: number;
    estado: string;
}

const GerenteDashboard: React.FC = () => {
    const [user, setUser] = useState<UserInfo | null>(null);
    const [cuentas, setCuentas] = useState<CuentaInfo[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [lastUpdate, setLastUpdate] = useState<Date>(new Date());
    const navigate = useNavigate();

    useEffect(() => {
        const loggedUser = localStorage.getItem('user');
        if (!loggedUser) {
            navigate('/');
            return;
        }
        const parsedUser: UserInfo = JSON.parse(loggedUser);
        setUser(parsedUser);
        fetchCuentas(parsedUser.idSucursal);

        // Configurar Polling cada 3 segundos (Simulación Real-Time)
        const interval = setInterval(() => {
            fetchCuentas(parsedUser.idSucursal);
        }, 3000);

        return () => clearInterval(interval);
    }, [navigate]);

    const fetchCuentas = async (idSucursal: number) => {
        try {
            const data = await listarCuentasPorSucursal(idSucursal);
            setCuentas(data);
            setLastUpdate(new Date());
        } catch (err) {
            console.error("Error fetching accounts:", err);
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('user');
        navigate('/');
    };

    if (!user) return null;

    return (
        <div className="min-h-screen bg-eureka-bg animate-fade-in text-gray-700">
            {/* Header / Navbar */}
            <header className="bg-eureka-primary text-white shadow-lg">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between h-20 items-center">
                        <div className="flex items-center gap-3">
                            <div className="bg-white/20 p-2 rounded-lg backdrop-blur-sm">
                                <Landmark className="w-8 h-8 text-white" />
                            </div>
                            <div>
                                <h1 className="text-xl font-black tracking-tight uppercase text-white m-0 leading-tight">Eureka Bank</h1>
                                <p className="text-xs opacity-80 font-bold tracking-widest text-white m-0">{user.nombreSucursal}</p>
                            </div>
                        </div>
                        
                        <div className="flex items-center gap-6">
                            <div className="text-right hidden md:block border-r border-white/20 pr-6">
                                <p className="text-sm opacity-70 m-0">Bienvenido, Gerente</p>
                                <p className="font-bold m-0 text-white">{user.nombre}</p>
                            </div>
                            <button 
                                onClick={handleLogout}
                                className="flex items-center gap-2 bg-white/10 hover:bg-white/20 px-4 py-2 rounded-lg transition-all border border-white/10 text-white font-bold text-sm cursor-pointer"
                            >
                                <LogOut size={18} />
                                <span>Cerrar Sesión</span>
                            </button>
                        </div>
                    </div>
                </div>
            </header>

            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
                {/* Stats / Overview Cards */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 animate-slide-up">
                    <div className="card p-6 flex items-center gap-4 bg-gradient-to-br from-white to-eureka-light">
                        <div className="p-4 bg-eureka-primary/10 rounded-xl text-eureka-primary">
                            <Users size={28} />
                        </div>
                        <div>
                            <p className="text-sm font-bold text-gray-500 uppercase tracking-tight m-0">Total Cuentas</p>
                            <p className="text-3xl font-black text-eureka-primary m-0">{cuentas.length}</p>
                        </div>
                    </div>
                    <div className="card p-6 flex items-center gap-4 bg-white">
                        <div className="p-4 bg-eureka-success/10 rounded-xl text-eureka-success">
                            <Activity size={28} />
                        </div>
                        <div>
                            <p className="text-sm font-bold text-gray-500 uppercase tracking-tight m-0">Disponibles</p>
                            <p className="text-3xl font-black text-eureka-success m-0">
                                {cuentas.filter(c => c.estado === 'LIBRE').length}
                            </p>
                        </div>
                    </div>
                    <div className="card p-6 flex items-center gap-4 bg-white">
                        <div className="p-4 bg-eureka-error/10 rounded-xl text-eureka-error">
                            <RefreshCcw className="animate-spin-slow" size={28} />
                        </div>
                        <div>
                            <p className="text-sm font-bold text-gray-500 uppercase tracking-tight m-0">En Transacción</p>
                            <p className="text-3xl font-black text-eureka-error m-0">
                                {cuentas.filter(c => c.estado === 'OCUPADA').length}
                            </p>
                        </div>
                    </div>
                </div>

                {/* MashUp Monitor Table */}
                <div className="card bg-white animate-slide-up" style={{ animationDelay: '0.1s' }}>
                    <div className="px-8 py-6 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
                        <div className="text-left">
                            <h2 className="text-xl font-extrabold text-eureka-primary flex items-center gap-2 m-0">
                                <CreditCard size={24} />
                                Monitoreo de Operaciones (RESTful)
                            </h2>
                            <p className="text-sm text-gray-400 font-medium m-0">Visualización de estados y saldos por sucursal</p>
                        </div>
                        <div className="flex items-center gap-3 text-xs font-bold text-gray-400 bg-white px-4 py-2 rounded-full shadow-sm border">
                            <div className="w-2 h-2 bg-eureka-success rounded-full animate-pulse"></div>
                            ÚLTIMA ACTUALIZACIÓN: {lastUpdate.toLocaleTimeString()}
                        </div>
                    </div>

                    <div className="overflow-x-auto">
                        <table className="w-full text-left">
                            <thead>
                                <tr className="bg-gray-50 text-gray-400 uppercase text-xs tracking-widest font-black border-b border-gray-100">
                                    <th className="px-8 py-5">Número de Cuenta</th>
                                    <th className="px-8 py-5">Titular</th>
                                    <th className="px-8 py-5">Saldo Actual</th>
                                    <th className="px-8 py-5 text-center">Estado</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100">
                                {loading && cuentas.length === 0 ? (
                                    <tr>
                                        <td colSpan={4} className="px-8 py-12 text-center text-gray-400">
                                            <div className="flex flex-col items-center gap-3">
                                                <Loader2 className="animate-spin text-eureka-primary" size={40} />
                                                <p className="font-bold">Cargando datos del sistema...</p>
                                            </div>
                                        </td>
                                    </tr>
                                ) : (
                                    cuentas.map((cuenta, idx) => {
                                        const isLibre = cuenta.estado === 'LIBRE';
                                        return (
                                            <tr key={idx} className="hover:bg-eureka-light/30 transition-colors group">
                                                <td className="px-8 py-5 font-black text-eureka-primary tracking-tight">
                                                    {cuenta.numero}
                                                </td>
                                                <td className="px-8 py-5 text-gray-600 font-medium group-hover:text-gray-900 transition-colors">
                                                    {cuenta.titular}
                                                </td>
                                                <td className="px-8 py-5 font-bold text-gray-700">
                                                    <span className="text-eureka-primary opacity-60 mr-1">S/</span>
                                                    {cuenta.saldo.toLocaleString('es-PE', { minimumFractionDigits: 2 })}
                                                </td>
                                                <td className="px-8 py-5 text-center">
                                                    <span className={`status-badge ${isLibre ? 'status-available' : 'status-blocked'} inline-flex items-center gap-1.5`}>
                                                        <span className={`w-2 h-2 rounded-full ${isLibre ? 'bg-eureka-success' : 'bg-eureka-error'}`}></span>
                                                        {isLibre ? 'Disponible' : 'En Proceso'}
                                                    </span>
                                                </td>
                                            </tr>
                                        )
                                    })
                                )}
                                {!loading && cuentas.length === 0 && (
                                    <tr>
                                        <td colSpan={4} className="px-8 py-12 text-center text-gray-400 font-bold">
                                            No se encontraron cuentas activas en esta sucursal.
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </main>

            <footer className="max-w-7xl mx-auto px-8 py-12 text-center">
                <div className="inline-block px-6 py-2 bg-white rounded-full shadow-sm border border-gray-100">
                   <p className="text-[10px] text-gray-400 font-black uppercase tracking-widest m-0">
                       Sistema de Monitoreo Eureka Bank - Protocolo JAX-RS REST 1.0
                   </p>
                </div>
            </footer>

            <style dangerouslySetInnerHTML={{ __html: `
                @keyframes spin-slow {
                    from { transform: rotate(0deg); }
                    to { transform: rotate(360deg); }
                }
                .animate-spin-slow {
                    animation: spin-slow 8s linear infinite;
                }
            `}} />
        </div>
    );
};

interface LoaderProps {
    size: number;
    className?: string;
}

const Loader2: React.FC<LoaderProps> = ({ size, className }) => (
    <svg 
        xmlns="http://www.w3.org/2000/svg" 
        width={size} 
        height={size} 
        viewBox="0 0 24 24" 
        fill="none" 
        stroke="currentColor" 
        strokeWidth="2" 
        strokeLinecap="round" 
        strokeLinejoin="round" 
        className={className}
    >
        <path d="M21 12a9 9 0 1 1-6.219-8.56" />
    </svg>
);

export default GerenteDashboard;
