// 
// Decompiled by Procyon v0.5.36
// 

package me.travis.wurstplus.util;

import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.BinaryOperator;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import java.util.Collection;

public class Immutables
{
    public static <T> Collection<T> copy(@Nullable final Collection<T> collection) {
        return copyToList(collection);
    }
    
    public static <T> List<T> copyToList(@Nullable final Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyList();
        }
        if (collection instanceof ImmutableList) {
            return (List<T>)(List)collection;
        }
        if (collection.size() == 1) {
            return Collections.singletonList(collection.iterator().next());
        }
        return (List<T>)ImmutableList.copyOf((Collection)collection);
    }
    
    public static <T> Set<T> copyToSet(@Nullable final Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptySet();
        }
        if (collection.size() == 1) {
            return Collections.singleton(collection.iterator().next());
        }
        return (Set<T>)ImmutableSet.copyOf((Collection)collection);
    }
    
    public static <T> Collector<T, ImmutableSet.Builder<T>, ImmutableSet<T>> toImmutableSet() {
        return new ImmutableSetCollector<T>();
    }
    
    public static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> toImmutableList() {
        return new ImmutableListCollector<T>();
    }
    
    private static class ImmutableSetCollector<E> implements Collector<E, ImmutableSet.Builder<E>, ImmutableSet<E>>
    {
        @Override
        public Supplier<ImmutableSet.Builder<E>> supplier() {
            return ImmutableSet::builder;
        }
        
        @Override
        public BiConsumer<ImmutableSet.Builder<E>, E> accumulator() {
            return ImmutableSet.Builder::add;
        }
        
        @Override
        public BinaryOperator<ImmutableSet.Builder<E>> combiner() {
            return (BinaryOperator<ImmutableSet.Builder<E>>)((l, r) -> {
                l.addAll((Iterable)r.build());
                return l;
            });
        }
        
        @Override
        public Function<ImmutableSet.Builder<E>, ImmutableSet<E>> finisher() {
            return ImmutableSet.Builder::build;
        }
        
        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.UNORDERED);
        }
    }
    
    private static class ImmutableListCollector<E> implements Collector<E, ImmutableList.Builder<E>, ImmutableList<E>>
    {
        @Override
        public Supplier<ImmutableList.Builder<E>> supplier() {
            return ImmutableList::builder;
        }
        
        @Override
        public BiConsumer<ImmutableList.Builder<E>, E> accumulator() {
            return ImmutableList.Builder::add;
        }
        
        @Override
        public BinaryOperator<ImmutableList.Builder<E>> combiner() {
            return (BinaryOperator<ImmutableList.Builder<E>>)((l, r) -> {
                l.addAll((Iterable)r.build());
                return l;
            });
        }
        
        @Override
        public Function<ImmutableList.Builder<E>, ImmutableList<E>> finisher() {
            return ImmutableList.Builder::build;
        }
        
        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }
}
