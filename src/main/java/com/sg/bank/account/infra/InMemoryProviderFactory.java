package com.sg.bank.account.infra;

import io.rabitka.core.ddd.IEntity;
import io.rabitka.repository.core.provider.Provider;
import io.rabitka.repository.core.provider.ProviderFactory;
import io.rabitka.repository.provider.memory.InMemoryProvider;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class InMemoryProviderFactory implements ProviderFactory {

    @Override
    public <AR extends IEntity<ID>, ID extends Serializable> Provider<AR, ID> byAggregateRoot(Class<AR> aggregateRoot){
        return new InMemoryProvider<>();
    }

}
