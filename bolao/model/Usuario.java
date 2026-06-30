package bolao.model;

public abstract class Usuario {
    private static int contadorId = 1;

    private int id;
    private String nome;
    private String email;
    private String senha;

    public Usuario(String nome, String email, String senha) {
        this.id = contadorId++;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public boolean autenticar(String emailTentativa, String senhaTentativa) {
        return this.email.equals(emailTentativa) && this.senha.equals(senhaTentativa);
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }

    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " <" + email + ">";
    }
}
