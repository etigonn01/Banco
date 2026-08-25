package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.AccountRequestDTO;
import com.siqueiros.bank.dto.AccountResponseDTO;
import com.siqueiros.bank.exception.AccountAlreadyExistsException;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.mappers.AccountMapper;
import com.siqueiros.bank.model.Account;
import com.siqueiros.bank.repositories.AccountRepository;
import com.siqueiros.bank.repositories.TypeAccountRepository;
import com.siqueiros.bank.repositories.UserRepository;
import com.siqueiros.bank.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService
{
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TypeAccountRepository typeAccountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            UserRepository userRepository,
            TypeAccountRepository typeAccountRepository,
            AccountMapper accountMapper)
    {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.typeAccountRepository = typeAccountRepository;
        this.accountMapper = accountMapper;
    }

    /**
     * Devuelve todas las cuentas bancarias activas la base de datos
     * @return Una lista {@link List} de tipo {@link AccountResponseDTO} con los registros activos en la base de datos
     */
    @Override
    public List<AccountResponseDTO> findAllActiveAccounts() {

        return accountRepository.findAllActiveWithRelations()
                .stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    /**
     * Devuelve la cuenta bancaria activa con Id
     * @param accountId de la cuenta que se busca en la base de datos
     * @return Un objeto {@link AccountResponseDTO} con la información del registro activo encontrado
     * @throws EntityNotFoundException Si el accountId no corresponde a ninguna cuenta en la base de datos
     */
    @Override
    public AccountResponseDTO findActiveAccountById(Long accountId) {
        return accountRepository.findActiveByIdWithRelations(accountId)
                .map(accountMapper::toResponse)
                .orElseThrow(() -> EntityNotFoundException.of("Cuenta",  accountId));
    }

    /**
     * Registra una cuenta bancaria en la base de datos
     * @param accountRequestDTO Es el objeto que llega en la petición con la información para dar de alta una cuenta bancaria
     * @return {@link AccountResponseDTO} - Es el objeto de respuesta con la información del usuario registrado
     * @throws AccountAlreadyExistsException Si el usuario ya tiene registrada una cuenta del mismo tipo
     * @throws EntityNotFoundException Si el userId no corresponde a ningun usuario en la base de datos
     * @throws EntityNotFoundException Si el typeAccountId no corresponde a ningun tipo de cuenta de la base de datos
     */
    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        boolean accountAlreadyExist = accountRepository
                .existsActiveByUserIdAndTypeAccountId(accountRequestDTO.userId(), accountRequestDTO.typeAccountId());

        if (accountAlreadyExist) {
            throw AccountAlreadyExistsException.of(accountRequestDTO.userId());
        }

        var user = userRepository.findById(accountRequestDTO.userId())
                .orElseThrow(() -> EntityNotFoundException.of("Usuario", accountRequestDTO.userId()));

        var typeAccount = typeAccountRepository.findById(accountRequestDTO.typeAccountId())
                .orElseThrow(() -> EntityNotFoundException.of("Tipo de cuenta", accountRequestDTO.typeAccountId()));

        Account newAccount = new Account(accountRequestDTO.balance(), typeAccount, user);
        var accountSaved = accountRepository.save(newAccount);
        return accountMapper.toResponse(accountSaved);
    }

    /**
     * Elimina de manera lógica una cuenta bancaria de la base de datos
     *
     * @throws EntityNotFoundException Si el accountId no corresponde a ninguna cuenta bancaria en la base de datos
     */
    @Override
    public void logicalDelete(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> EntityNotFoundException.of("Cuenta", accountId));
        account.close();
        accountRepository.save(account);
    }
}
