package kipoderax.virtuallotto.dtos.mapper;

public interface Mapper<F, T> {

    T map(F from);

}