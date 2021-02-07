package bo.edu.uagrm.ficct.inf310sb.arboles.Excepciones;

public class ExcepcionOrdenInvalido extends Exception {

    public ExcepcionOrdenInvalido() {
        super("Arbol Orden Invalido");
    }

    public ExcepcionOrdenInvalido(String message) {
        super(message);
    }
}
