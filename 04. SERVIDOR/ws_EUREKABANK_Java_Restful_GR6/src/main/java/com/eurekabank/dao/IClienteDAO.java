package com.eurekabank.dao;

import com.eurekabank.modelo.Cliente;

public interface IClienteDAO {
    Cliente validarLogin(String dni);
}
