import React, {useState} from 'react';
import { StyleSheet, Text, View } from 'react-native';

  //const Texto = (props) =>{
   // const {children,style} = props
   // return(
    // <Text style={[styles.red,style]}>{children}</Text>
   // )
  //}
const Micomponente= () =>{
const [texto, setTexto]=useState ('Valor Original')
const actualizaTexto = () =>{
  setTexto('Haz presionado el Texto')
}
  return(
    <Text onPress={actualizaTexto}>{texto}</Text>
  )

}
  export default function App() {
  return (
    <View style={styles.container}>
      <Micomponente/>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#ab5757ff',
    alignItems: 'center',
    justifyContent:'center',
    flexDirection:'column',
  },
 
});
