package bo.edu.uagrm.ficct.inf310sb.arboles.Excepciones;

public class ExcepcionClaveNoExiste  extends RuntimeException{
    public ExcepcionClaveNoExiste() {
        this("clave no existe en su estructrura");
    }

    public ExcepcionClaveNoExiste(String message) {
        super(message);
    }
}
