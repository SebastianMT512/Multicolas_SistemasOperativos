
package procesosbloqueo;



    public class ListaCircular {

        private Nodo cabeza;
        private Nodo ultimo;
        private int tamaño;
        private String cadenaLista;
        private boolean SRTF;

        ListaCircular(){

           this.cabeza = null; 
           this.ultimo = null; 
           this.tamaño = 0;

        }
        public boolean estaVacia() {
            return cabeza == null;
        }
        public void desocupar(){
            cabeza=null;
            ultimo=null;
            tamaño = 0;
        }
        void insertar(String s, int r, int l, int i, boolean ordenar){
            Nodo nuevo = new Nodo(s, r, l, i);
            
            if(ordenar == true){
                
                if (cabeza == null) {
                    
                    cabeza = nuevo;
                    ultimo = nuevo;
                    cabeza.setSiguiente(ultimo);
                    
                    } else if (nuevo.getRafaga() < cabeza.getRafaga()) {
                        nuevo.setSiguiente(cabeza);
                        cabeza = nuevo;
                        ultimo.setSiguiente(cabeza);
                    } else {
                        Nodo actual = cabeza;
                        while (actual.getSiguiente() != cabeza && actual.getSiguiente().getRafaga() < nuevo.getRafaga()) {
                            actual = actual.getSiguiente();
                        }
                        nuevo.setSiguiente(actual.getSiguiente());
                        actual.setSiguiente(nuevo);
                        if (nuevo.getSiguiente() == cabeza) {
                            ultimo = nuevo;
                        }
                          
                    }
                tamaño++; 
            }else if ( ordenar == false){
                Nodo sig = null;

                if(cabeza == null){

                    cabeza = nuevo;
                    ultimo = nuevo;
                    cabeza.setSiguiente(ultimo);

                } else {
                    ultimo.setSiguiente(nuevo);
                    nuevo.setSiguiente(cabeza);
                    ultimo = nuevo;
                }
                tamaño++;   
            }
        
}
        public void setCabeza(Nodo cabeza) {
            this.cabeza = cabeza;
        }

        public void eliminar(Nodo nodoAtendido){

        Nodo actual = cabeza;
            

            do {
                
                if (actual.equals(nodoAtendido)) {
                    if (actual == cabeza) {
                        cabeza = actual.getSiguiente();
                        ultimo.setSiguiente(cabeza);
                    } else {
                        Nodo nodoAnterior = cabeza;
                        while (nodoAnterior.getSiguiente() != actual) {
                            nodoAnterior = nodoAnterior.getSiguiente();
                        }
                        nodoAnterior.setSiguiente(actual.getSiguiente());
                    }

                    actual = null;
                    tamaño--;
                    return;
                }

                actual = actual.getSiguiente();
            } while (actual != cabeza);
        }

        public void intercambiar(Nodo nodoAtendido){
    
            cabeza = nodoAtendido.getSiguiente();
            ultimo.setSiguiente(nodoAtendido);

            ultimo = nodoAtendido;
            ultimo.setSiguiente(cabeza);

        }

        public int getTamaño() {
            return tamaño;
        }

        public Nodo getCabeza() {
            return cabeza;
        }

        public Nodo getUltimo() {
            return ultimo;
        }

        public void setUltimo(Nodo ultimo) {
            this.ultimo = ultimo;
        }

    }
