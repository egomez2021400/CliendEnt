package org.edgargomez.bean;

public class Login {
    
    private String UsuarioMaster;
    private String passwordLogin;

    public Login() {
    }

    public Login(String UsuarioMaster, String passwordLogin) {
        this.UsuarioMaster = UsuarioMaster;
        this.passwordLogin = passwordLogin;
    }

    public String getUsuarioMaster() {
        return UsuarioMaster;
    }

    public void setUsuarioMaster(String UsuarioMaster) {
        this.UsuarioMaster = UsuarioMaster;
    }

    public String getPasswordLogin() {
        return passwordLogin;
    }

    public void setPasswordLogin(String passwordLogin) {
        this.passwordLogin = passwordLogin;
    }
}
