package com.novoda.noplayer.model;

public abstract class Either<L, R> {

    public static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    public static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    Either() {
        // restrict subclasses to the package
    }

    public abstract void apply(Consumer<L> leftConsumer, Consumer<R> rightConsumer);

    static class Left<L, R> extends Either<L, R> {

        private final L valueLeft;

        Left(L valueLeft) {
            this.valueLeft = valueLeft;
        }

        @Override
        public void apply(Consumer<L> leftConsumer, Consumer<R> rightConsumer) {
            leftConsumer.accept(valueLeft);
        }
    }

    static class Right<L, R> extends Either<L, R> {

        private final R valueRight;

        Right(R valueRight) {
            this.valueRight = valueRight;
        }

        @Override
        public void apply(Consumer<L> leftConsumer, Consumer<R> rightConsumer) {
            rightConsumer.accept(valueRight);
        }
    }

    public interface Consumer<T> {
        void accept(T value);
    }
}
