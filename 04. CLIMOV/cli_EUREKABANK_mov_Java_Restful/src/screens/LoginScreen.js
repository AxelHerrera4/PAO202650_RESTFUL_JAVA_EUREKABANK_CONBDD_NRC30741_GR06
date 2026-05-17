import React, { useState } from 'react';
import { 
    View, 
    Text, 
    TextInput, 
    TouchableOpacity, 
    StyleSheet, 
    KeyboardAvoidingView, 
    Platform,
    ActivityIndicator,
    Alert
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { restMobileService } from '../services/restMobileService';
import { User } from 'lucide-react-native';

const LoginScreen = ({ onLoginSuccess }) => {
    const [usuario, setUsuario] = useState('');
    const [loading, setLoading] = useState(false);

    const handleLogin = async () => {
        if (!usuario) {
            Alert.alert("Error", "Por favor ingresa tu DNI.");
            return;
        }

        setLoading(true);
        try {
            const data = await restMobileService.login(usuario);
            await AsyncStorage.setItem('userData', JSON.stringify(data));
            onLoginSuccess(data);
        } catch (error) {
            Alert.alert("Error de Acceso", error.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <SafeAreaView style={styles.container}>
            <KeyboardAvoidingView 
                behavior={Platform.OS === "ios" ? "padding" : "height"}
                style={styles.content}
            >
                <View style={styles.header}>
                    <Text style={styles.bankName}>Eureka Bank</Text>
                    <Text style={styles.subtitle}>Banca Móvil para Clientes (RESTful)</Text>
                </View>

                <View style={styles.form}>
                    <Text style={styles.label}>Ingresa tu DNI</Text>
                    <View style={styles.inputContainer}>
                        <User size={22} color="#0066CC" style={styles.inputIcon} />
                        <TextInput 
                            style={styles.input}
                            placeholder="Número de Identidad"
                            value={usuario}
                            onChangeText={setUsuario}
                            autoCapitalize="none"
                            keyboardType="numeric"
                        />
                    </View>

                    <TouchableOpacity 
                        style={[styles.button, loading && styles.buttonDisabled]} 
                        onPress={handleLogin}
                        disabled={loading}
                    >
                        {loading ? (
                            <ActivityIndicator color="#FFF" />
                        ) : (
                            <Text style={styles.buttonText}>INGRESAR A MI CUENTA</Text>
                        )}
                    </TouchableOpacity>
                </View>

                <View style={styles.footer}>
                    <Text style={styles.footerText}>© 2026 Eureka Bank S.A.</Text>
                </View>
            </KeyboardAvoidingView>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#FFFFFF',
    },
    content: {
        flex: 1,
        justifyContent: 'center',
        paddingHorizontal: 40,
    },
    header: {
        alignItems: 'center',
        marginBottom: 50,
    },
    bankName: {
        fontSize: 32,
        fontWeight: 'bold',
        color: '#0066CC',
        letterSpacing: 1,
    },
    subtitle: {
        fontSize: 16,
        color: '#666666',
        marginTop: 5,
    },
    label: {
        fontSize: 14,
        fontWeight: 'bold',
        color: '#333',
        marginBottom: 10,
        textAlign: 'center',
    },
    form: {
        width: '100%',
    },
    inputContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        borderBottomWidth: 1,
        borderBottomColor: '#E0E0E0',
        marginBottom: 25,
        paddingHorizontal: 5,
    },
    inputIcon: {
        marginRight: 10,
    },
    input: {
        flex: 1,
        height: 45,
        fontSize: 16,
        color: '#333',
    },
    button: {
        backgroundColor: '#0066CC',
        height: 50,
        borderRadius: 25,
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 20,
        shadowColor: "#000",
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        elevation: 3,
    },
    buttonDisabled: {
        backgroundColor: '#A0C4E8',
    },
    buttonText: {
        color: '#FFFFFF',
        fontSize: 16,
        fontWeight: 'bold',
    },
    footer: {
        position: 'absolute',
        bottom: 40,
        left: 0,
        right: 0,
        alignItems: 'center',
    },
    footerText: {
        color: '#999',
        fontSize: 12,
    }
});

export default LoginScreen;
