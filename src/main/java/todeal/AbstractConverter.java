package todeal;

public abstract class AbstractConverter<T> implements Converter{

    Validator validator = new DefaultValidator(); // 기본 값 주입 가능

    ConverterPostProcessor<T> converterPostProcessor = new DefaultConvertPostProcessor(); // 기본 값 주입 가능

    @Override
    public T convert(DealInfo dealInfo) {

        if (!validator.validate(dealInfo)) {
            // 예외처리
        }
        T t = toDeal(dealInfo);
        converterPostProcessor.converterPostProcessor(t);
        return t;
    }

    public abstract T toDeal(DealInfo dealInfo);

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public void setConverterPostProcessor(ConverterPostProcessor<T> converterPostProcessor) {
        this.converterPostProcessor = converterPostProcessor;
    }
}
