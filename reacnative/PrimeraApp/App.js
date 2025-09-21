import { StatusBar } from 'expo-status-bar';
import {StyleSheet, View } from 'react-native';
import { Map, Modal, Panel } from './componentes';
import Panel from './componentes/Panel';


export default function App() {
  return (
    <View style={styles.container}>
      <Map/>
      <Panel/>
      <Modal/>
   </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
