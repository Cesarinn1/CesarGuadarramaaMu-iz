import { StyleSheet, Button,View } from "react-native";

export default () => {
    return (
        <View>
            <Button title="Listar"/>
            <Button title="Mostra/Ocultar"/>
        </View>
    )
}

const styles =StyleSheet.create({
    panel:{
        flex:1,
        flexDirection:"row",
        justifyContent:"center",
        alignItems:"center",
    },
})