package todeal;

import java.util.Objects;

public class DefaultValidator implements Validator {
    @Override
    public boolean validate(DealInfo dealInfo) {
        return Objects.nonNull(dealInfo);
    }
}
