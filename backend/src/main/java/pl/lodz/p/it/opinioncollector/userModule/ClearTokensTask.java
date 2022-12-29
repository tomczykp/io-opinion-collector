package pl.lodz.p.it.opinioncollector.userModule;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.opinioncollector.userModule.token.Token;
import pl.lodz.p.it.opinioncollector.userModule.token.TokenRepository;

import java.time.Instant;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
@Transactional
public class ClearTokensTask {

    private final TokenRepository repository;

    private final Logger logger = Logger.getLogger("pl.lodz.p.it.opinioncollector.userModule.ClearTokensTask");

    @Scheduled(cron = "0 0 3 * * *")
    protected void task() {
        List<Token> tokens = this.repository.findAll();
        int i = 0;
        for (Token t : tokens) {
            if (t.getExpiresAt().isBefore(Instant.now())) {
                i++;
                this.repository.deleteByToken(t.getToken());
            }
        }
        logger.log(Level.INFO, "Deleted " + i + " tokens.");
    }
}
