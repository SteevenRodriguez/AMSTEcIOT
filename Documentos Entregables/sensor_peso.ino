#include <WISOL.h>
#include <Wire.h>
#include <math.h>
#include "HX711.h"

// definimos variables de entrada
#define DOUT  A1
#define CLK  A0
#define ANALOGPILA A3


Isigfox *Isigfox = new WISOL();
typedef union{
uint16_t number;
uint8_t bytes[2];
} UINT16_t;

//se instacia el metodo de la clase
HX711 balanza(DOUT, CLK);

// se declara variables globales para obtener la bateria
int analogValor = 0; // variable para contar el tiempo de envio de bateria
float previousMillis = 0;     // variable para setear el inicio del tiempo   
float intervaloMaximo = 60000; // variable para setear el máximo tiempo 
int voltaje = 0;		// variable para obtener el voltaje de la bateria
int porcentaje_voltaje=0;       // variable para obtener el porcentaje de bateria
boolean tiempo = false;		// variable
float volt=0;

// variables globales del proceso 
float peso; // para obtener el peso
int categoria; // setear categoria
int bateria;   // para obtener el porcentaje de bateria
float peso_anterior=0;
float notificacion= 0;

// se inicializa las variables
void setup() {
  Wire.begin();
  Wire.setClock(100000);
  Serial.begin(9600);
  

  // WISOL modem test
  Isigfox->initSigfox();
  Isigfox->testComms();
  //Serial.println(balanza.read());
  Serial.print("Lectura del valor del ADC:  ");
  Serial.println(balanza.read());
  Serial.println("No ponga ningun  objeto sobre la balanza");
  Serial.println("Destarando...");
  Serial.println("...");
  balanza.set_scale(439430.25);
  balanza.tare(20);  //Reset the scale to 0
  Serial.println("Listo para pesar"); 

 
}

void loop() {
  peso = obtenerPeso();  
  bateria = obtener_porcentaje_bateria();
  categoria =  obtenerCategoria(peso);
  Serial.print("Peso: ");
  Serial.print(peso);
  Serial.println(" kg"); 
  
  Serial.print("Categoria: ");
  Serial.println(categoria);
  
  if ((peso <= peso_anterior-0.01) or (peso >= peso_anterior+0.01)){ 
    byte *float_byte = (byte *)&peso;
    byte *float_byte2 = (byte *)&categoria;
	byte *float_byte3 = (byte *)&bateria;
    const uint8_t payloadSize = 9;
    uint8_t buf_str[payloadSize];
    
    buf_str[0] = float_byte3[0];
    buf_str[1] = float_byte3[1];
    buf_str[2] = float_byte[0];
    buf_str[3] = float_byte[1];
    buf_str[4] = float_byte[2];
    buf_str[5] = float_byte[3];
    buf_str[6] = float_byte2[0];
    buf_str[7] = float_byte2[1];
  
    Send_Pload(buf_str, payloadSize);
    Serial.println("_Se envia");
    //delay(20000);
  }
  peso_anterior=peso;

  tiempo = retardo();
  if (tiempo==true){
          
		envio_porcentaje();
  } 
 }

// funcion para obtener el peso del objeto colocado en la balanza
float obtenerPeso(){
  //Serial.print("Peso: ");
  float peso1 ;
  peso1 = (balanza.get_units(20)*-1);
  //Serial.println(" kg");
  delay(500);

  if (peso1 < 0){
    peso1 = 0;
  }
  
  return peso1;
}

// funcion para obtener la categoria de acuerdo a un perfil
int obtenerCategoria(float peso){
  int categoria;
  
  if (peso <= 0.01){
    categoria = 11;//nada en la balanza
    }
  else if (peso >= 0.12 and peso< 0.174){
    categoria = 2;//celular
    }
    else if (peso>= 0.290 and peso <= 0.35){
      categoria = 7;//vaso
    }
    else if ((peso>= 0.225 and peso <= 0.275) or (peso>= 0.35 and peso <= 0.38)){
      categoria = 9;//plato
    }
    else if (peso>= 1.5 and peso <= 2.8){
      categoria =8;//laptop
    }
    else if (peso>= 0.41 and peso <= 0.58){
  categoria =5;//cuaderno
    }
    else{
        categoria =10;//desconocido
    }
    return categoria;
}

// funcion para el envio de datos a sigfox
  void Send_Pload(uint8_t *sendData, int len) {
    recvMsg *RecvMsg;
    RecvMsg = (recvMsg *)malloc(sizeof(recvMsg));
    Isigfox->sendPayload(sendData, len, 0, RecvMsg);
    for (int i = 0; i < RecvMsg->len; i++) {
      Serial.print(RecvMsg->inData[i]); 
    }
    Serial.println("");
    free(RecvMsg);
}

// funcion para cintabilizar el tiempo

boolean retardo(){
      float currentMillis = millis();
      if(currentMillis - previousMillis > intervaloMaximo) {  //Cuando se cumplen los 10 minutos
            previousMillis = currentMillis;   
            return true; 
      }else{
         return false;
      }
  }

// funcion para obtener el porcentaje de bateria
 int obtener_porcentaje_bateria(){
    analogValor = analogRead(ANALOGPILA); 
    // Obtenemos el voltaje
    voltaje = int(0.0048 * analogValor);
    porcentaje_voltaje = int (voltaje * 22.22); 
    Serial.print("Voltaje: ");
    Serial.println(voltaje);
    Serial.print("Porcentaje Voltaje: ");
    Serial.println(porcentaje_voltaje);
    return porcentaje_voltaje;
 }

// funcion para enviar el porcentaje de bateria al backend de sigfox
 void envio_porcentaje(){
    volt = int (obtener_porcentaje_bateria());
    byte *float_byte = (byte *)&volt;
    const uint8_t payloadSize = 3;
    uint8_t buf_str[payloadSize];
    buf_str[0] = float_byte[0];
    buf_str[1] = float_byte[1];
  
    Send_Pload(buf_str, payloadSize);
    Serial.println("_Se envia bateria");
 }
