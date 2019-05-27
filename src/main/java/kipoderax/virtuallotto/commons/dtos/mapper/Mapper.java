package kipoderax.virtuallotto.commons.dtos.mapper;

public interface Mapper<F, T> {

    T map(F from);

}