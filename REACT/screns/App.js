import React, { useEffect } from 'react';
import { StatusBar } from 'expo-status-bar';
import { Dimensions, StyleSheet, Text, View, Button, Alert } from 'react-native';
import * as Location from 'expo-location';
import MapView, { Marker } from 'react-native-maps';

// Importaciones de navegación
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

const Stack = createNativeStackNavigator();

// 🔹 Pantalla de inicio
function HomeScreen({ navigation }) {
  return (
    <View style={styles.container}>
      <Text style={{ fontSize: 20, marginBottom: 20 }}>Pantalla Home</Text>
      <Button title="Ir al Mapa" onPress={() => navigation.navigate("Mapa")} />
      <Button title="Ir al Perfil" onPress={() => navigation.navigate("Perfil")} />
    </View>
  );
}

// 🔹 Pantalla del mapa
function MapScreen() {
  const buscaLocation = async () => {
    const { status } = await Location.requestForegroundPermissionsAsync();
    if (status !== 'granted') {
      return Alert.alert('Permiso denegado');
    }
    const location = await Location.getCurrentPositionAsync({});
    console.log(location);
  };

  useEffect(() => {
    buscaLocation();
  }, []);

  return (
    <View style={styles.container}>
      <Text style={{ marginBottom: 10 }}>Pantalla del Mapa</Text>
      <MapView style={styles.mapa}>
        <Marker
          coordinate={{
            latitude: 19.1562871,
            longitude: -96.1289567
          }}
          title="El Estarbocks"
          description="Esto es el Estarbocks"
        />
      </MapView>
      <StatusBar style="auto" />
    </View>
  );
}

// 🔹 Pantalla de perfil
function ProfileScreen() {
  return (
    <View style={styles.container}>
      <Text style={{ fontSize: 20 }}>Pantalla de Perfil</Text>
    </View>
  );
}

// 🔹 App principal con navegación
export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Home">
        <Stack.Screen name="Home" component={HomeScreen} options={{ title: "Inicio" }} />
        <Stack.Screen name="Mapa" component={MapScreen} />
        <Stack.Screen name="Perfil" component={ProfileScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

const styles = StyleSheet.create({
  mapa: {
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height / 2, // mitad de la pantalla
  },
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
