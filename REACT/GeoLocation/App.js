import React,{useEffect} from 'react';
import { StatusBar } from 'expo-status-bar';
import { Dimensions, StyleSheet, Text, View } from 'react-native';
import * as Location from 'expo-location';
import MapView, { Marker } from 'react-native-maps';

export default function App() {

  const buscaLocation =async ()=>{
    const {status}= await Location.requestForegroundPermissionsAsync()
    if(status!=='granted'){
      return Alert.alert('Permiso denegado')
    }
    const location= await Location.getCurrentPositionAsync({})
      console.log(location)
  }

  userEffect(()=> {
    buscaLocation()
  })

  return (
    <View style={styles.container}>
      <Text>Open up App.js to start working on your app!</Text>
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

const styles = StyleSheet.create({
  mapa:{
    width:Dimensiones.get('window').width,
    height: Dimensiones.get('window').height,
  },
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});