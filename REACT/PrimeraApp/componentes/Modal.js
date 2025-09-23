import { Modal, StyleSheet, View,text } from 'react-native';
export default()=>{
    return(
         <Modal
        animationType='slide'
       transparent= {true}
       visible={true}>
        <view style={styles.center}>
           <view style={styles.modalView}>
              <text>❤️</text>
          </view> 
        </view> 
    </Modal>
    )
}

const styles = StyleSheet.create({
     center:{
    flex:1,
    justifyContent:'center',
    alignItems:'center',
  },
  modalView:{
    backgroundColor:('#fff'),
    borderRadius:6,
    padding:20,
    shadowColor:("#000"),
    shadowOffset:{
      width:0,
      height:5,
    }
  },
})