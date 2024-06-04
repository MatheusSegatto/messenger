package Exceptions;

public class Exceptions {
    public static final IllegalArgumentException invalid_value = new IllegalArgumentException("Valor invalido");
    public static final IllegalArgumentException maxLimit_acomodacao = new IllegalArgumentException(
                    "Ultrapassou o numero máximo de hospedes");
    public static final IllegalArgumentException notAvaiable_acomodacao = new IllegalArgumentException(
                    "Acomodação indisponivel ");
    public static final IllegalArgumentException noCategory = new IllegalArgumentException(
                    "Essa categoria não existe");
    public static final IllegalArgumentException noTipoAcomodacao = new IllegalArgumentException(
                    "Essa categoria não existe");

    public static final IllegalArgumentException invalidCpf = new IllegalArgumentException("CPF invalido");
    public static final IllegalStateException cpfAlreadyExists = new IllegalStateException("CPF já cadastrado");
    public static final IllegalArgumentException noText = new IllegalArgumentException(
                    "Você precisa preencher todos os campos");
    public static final IllegalArgumentException paymentError = new IllegalArgumentException("Erro pagamento");

    public static final IllegalAccessError emptyField = new IllegalAccessError("É necessário preencher todos os campos");
    public static final IllegalAccessError alreadyExists = new IllegalAccessError("Já existe");
    public static final IllegalAccessError noExists = new IllegalAccessError("Não existe");
    public static final IllegalAccessError notSelected = new IllegalAccessError("Nenhuma linha selecionada!");
    public static final IllegalAccessError noPayment = new IllegalAccessError("Valor maior que o necessário");
    public static final IllegalAccessError notManutencao = new IllegalAccessError(
                    "O quarto está ocupado, precisa entrar em manutenção!");
    public static final IllegalAccessError alreadyCheckedOut = new IllegalAccessError(
                    "O CheckOut já foi feito nessa hospedagem!");


    public static final IllegalStateException serverNotRespondig = new IllegalStateException("[CLIENT]: Error trying to comunicate with Server Side!");
    public static final String Exceptions = null;
}
