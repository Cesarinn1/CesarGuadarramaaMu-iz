import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View, TextInput, Button } from 'react-native';
import React, { useState } from 'react';

export default function App() {
  const [user, setUser] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [color, setColor] = useState('red');

  // Usuario y contraseña válidos
  const validUser = "admin";
  const validPassword = "1234";

  const handleLogin = () => {
    if (user === validUser && password === validPassword) {
      setMessage("ACCESO CORRECTO");
      setColor("green");
    } else {
      setMessage("ACCESO DENEGADO");
      setColor("red");
    }
  };

  const handleCancel = () => {
    setUser('');
    setPassword('');
    setMessage('');
  };

  return (
    <View style={styles.container}>
      {/* Campo Usuario */}
      <TextInput
        style={styles.input}
        placeholder="User Name"
        value={user}
        onChangeText={setUser}
      />

      {/* Campo Contraseña */}
      <TextInput
        style={styles.input}
        placeholder="Password"
        value={password}
        onChangeText={setPassword}
        secureTextEntry={true}
      />

      {/* Botones */}
      <View style={styles.buttonContainer}>
        <Button title="LOGIN" onPress={handleLogin} />
        <Button title="CANCEL" onPress={handleCancel} color="gray" />
      </View>

      {/* Mensaje de acceso */}
      {message !== '' && (
        <Text style={[styles.message, { color: color }]}>{message}</Text>
      )}

      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  input: {
    width: '100%',
    height: 40,
    borderWidth: 1,
    borderColor: '#aaa',
    borderRadius: 5,
    paddingHorizontal: 10,
    marginBottom: 12,
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    width: '100%',
    marginBottom: 16,
  },
  message: {
    fontSize: 18,
    fontWeight: 'bold',
    textAlign: 'center',
    marginTop: 10,
  },
});
