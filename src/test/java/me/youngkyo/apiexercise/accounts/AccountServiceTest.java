package me.youngkyo.apiexercise.accounts;

import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void findByUsername() {

        HashSet<AccountRole> sets = Sets.newHashSet();
        sets.add(AccountRole.USER);
        sets.add(AccountRole.ADMIN);

        String username = "ykyo.jung@gmail.com";
        String password = "youngkyo";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(sets)
                .build();

        this.accountRepository.save(account);

        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertThat(userDetails.getPassword()).isEqualTo(password);
    }
}
