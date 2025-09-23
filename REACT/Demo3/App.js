import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';

export const Texto = (props) => {
  const { texto } = props;
  return (
    <Text>{texto}</Text>  // ¡Aquí estaba el error! (text → Text)
  );
};

export default function App() {
  return (
    <View style={styles.container}>
      <Text>Este es mi texto cambiado</Text>
      <Texto texto={`hola semi-dinamico`} />
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
  },
});