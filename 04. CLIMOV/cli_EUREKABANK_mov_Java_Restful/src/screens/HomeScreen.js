import React, { useState, useEffect, useCallback } from 'react';
import { 
    View, 
    Text, 
    StyleSheet, 
    FlatList, 
    TouchableOpacity,
    ActivityIndicator,
    RefreshControl,
    StatusBar
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { restMobileService } from '../services/restMobileService';
import { CreditCard, History, LogOut } from 'lucide-react-native';

const HomeScreen = ({ user, onLogout }) => {
    const [account, setAccount] = useState(null);
    const [movements, setMovements] = useState([]);
    const [loading, setLoading] = useState(true);
    const [refreshing, setRefreshing] = useState(false);

    // Función para obtener datos (se usará para el polling y refresh manual)
    const fetchData = useCallback(async (showLoading = false) => {
        if (showLoading) setLoading(true);
        try {
            // 1. Obtener estado de cuenta y saldo
            const accountInfo = await restMobileService.getAccountStatus(user.usuario);
            if (accountInfo) {
                setAccount(accountInfo);
                
                // 2. Si tenemos el número de cuenta, obtenemos los movimientos
                const history = await restMobileService.getExtracto(accountInfo.numero);
                setMovements(history);
            }
        } catch (error) {
            console.error("Error fetching data:", error);
        } finally {
            setLoading(false);
            setRefreshing(false);
        }
    }, [user.usuario]);

    // Polling de 3 segundos
    useEffect(() => {
        fetchData(true);
        const interval = setInterval(() => {
            fetchData(false); // Silencioso
        }, 3000);

        return () => clearInterval(interval);
    }, [fetchData]);

    const onRefresh = () => {
        setRefreshing(true);
        fetchData(false);
    };

    const renderMovement = ({ item }) => {
        const isRetiro = item.tipo.toLowerCase() === 'retiro';
        return (
            <View style={styles.movementItem}>
                <View style={styles.movementIcon}>
                    <Text style={styles.movementInitial}>{item.tipo.charAt(0)}</Text>
                </View>
                <View style={{ flex: 1 }}>
                    <Text style={styles.movementType}>{item.tipo}</Text>
                    <Text style={styles.movementDate}>{item.fecha}</Text>
                </View>
                <Text style={[
                    styles.movementAmount, 
                    { color: isRetiro ? '#E53935' : '#43A047' }
                ]}>
                    {isRetiro ? '-' : '+'}${item.monto}
                </Text>
            </View>
        );
    };

    return (
        <SafeAreaView style={styles.container}>
            <StatusBar barStyle="dark-content" />
            
            {/* CABECERA */}
            <View style={styles.header}>
                <View>
                    <Text style={styles.greeting}>Hola,</Text>
                    <Text style={styles.userName}>{user.nombre}</Text>
                </View>
                <TouchableOpacity onPress={onLogout} style={styles.logoutBtn}>
                    <LogOut size={22} color="#0066CC" />
                </TouchableOpacity>
            </View>

            {/* TARJETA MASHUP (SALDO + SEMAFORO) */}
            <View style={styles.cardContainer}>
                {loading && !account ? (
                    <ActivityIndicator color="#0066CC" />
                ) : account ? (
                    <View style={styles.card}>
                        <View style={styles.cardHeader}>
                            <CreditCard size={24} color="#FFF" />
                            <View style={[
                                styles.statusBadge, 
                                { backgroundColor: account.disponibilidad === 'VERDE' ? '#4CAF50' : '#F44336' }
                            ]}>
                                <View style={styles.statusDot} />
                                <Text style={styles.statusText}>
                                    {account.disponibilidad === 'VERDE' ? 'DISPONIBLE' : 'EN PROCESO'}
                                </Text>
                            </View>
                        </View>
                        
                        <Text style={styles.cardNumberLabel}>Número de Cuenta</Text>
                        <Text style={styles.cardNumber}>{account.numero}</Text>
                        
                        <View style={styles.balanceContainer}>
                            <Text style={styles.balanceLabel}>Saldo Actual</Text>
                            <Text style={styles.balanceValue}>${account.saldo}</Text>
                        </View>
                    </View>
                ) : (
                    <Text>No se encontró información de cuenta.</Text>
                )}
            </View>

            {/* HISTORIAL */}
            <View style={styles.historySection}>
                <View style={styles.sectionHeader}>
                    <History size={20} color="#333" />
                    <Text style={styles.sectionTitle}>Últimos Movimientos</Text>
                </View>

                <FlatList
                    data={movements}
                    keyExtractor={(item) => item.id}
                    renderItem={renderMovement}
                    contentContainerStyle={styles.listContent}
                    refreshControl={
                        <RefreshControl refreshing={refreshing} onRefresh={onRefresh} colors={['#0066CC']} />
                    }
                    ListEmptyComponent={
                        !loading && <Text style={styles.emptyText}>No hay movimientos registrados.</Text>
                    }
                />
            </View>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F8F9FA',
    },
    header: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingHorizontal: 25,
        paddingVertical: 20,
        backgroundColor: '#FFF',
    },
    greeting: {
        fontSize: 16,
        color: '#666',
    },
    userName: {
        fontSize: 22,
        fontWeight: 'bold',
        color: '#333',
    },
    logoutBtn: {
        padding: 8,
        borderRadius: 12,
        backgroundColor: '#F0F7FF',
    },
    cardContainer: {
        padding: 25,
    },
    card: {
        backgroundColor: '#0066CC',
        borderRadius: 20,
        padding: 25,
        shadowColor: "#0066CC",
        shadowOffset: { width: 0, height: 10 },
        shadowOpacity: 0.3,
        shadowRadius: 15,
        elevation: 10,
    },
    cardHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 20,
    },
    statusBadge: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingHorizontal: 10,
        paddingVertical: 5,
        borderRadius: 12,
    },
    statusDot: {
        width: 8,
        height: 8,
        borderRadius: 4,
        backgroundColor: '#FFF',
        marginRight: 6,
    },
    statusText: {
        color: '#FFF',
        fontSize: 10,
        fontWeight: 'bold',
    },
    cardNumberLabel: {
        color: '#C4E1FF',
        fontSize: 12,
        marginBottom: 4,
    },
    cardNumber: {
        color: '#FFF',
        fontSize: 18,
        fontWeight: '600',
        letterSpacing: 2,
        marginBottom: 25,
    },
    balanceContainer: {
        borderTopWidth: 1,
        borderTopColor: 'rgba(255,255,255,0.1)',
        paddingTop: 15,
    },
    balanceLabel: {
        color: '#C4E1FF',
        fontSize: 12,
    },
    balanceValue: {
        color: '#FFF',
        fontSize: 28,
        fontWeight: 'bold',
    },
    historySection: {
        flex: 1,
        backgroundColor: '#FFF',
        borderTopLeftRadius: 30,
        borderTopRightRadius: 30,
        paddingTop: 25,
        paddingHorizontal: 25,
    },
    sectionHeader: {
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: 20,
    },
    sectionTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333',
        marginLeft: 10,
    },
    listContent: {
        paddingBottom: 20,
    },
    movementItem: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingVertical: 15,
        borderBottomWidth: 1,
        borderBottomColor: '#F0F0F0',
    },
    movementIcon: {
        width: 40,
        height: 40,
        borderRadius: 20,
        backgroundColor: '#F0F7FF',
        justifyContent: 'center',
        alignItems: 'center',
        marginRight: 15,
    },
    movementInitial: {
        color: '#0066CC',
        fontWeight: 'bold',
        fontSize: 16,
    },
    movementType: {
        fontSize: 15,
        fontWeight: '600',
        color: '#333',
    },
    movementDate: {
        fontSize: 12,
        color: '#999',
    },
    movementAmount: {
        fontSize: 16,
        fontWeight: 'bold',
    },
    emptyText: {
        textAlign: 'center',
        color: '#999',
        marginTop: 40,
    }
});

export default HomeScreen;
