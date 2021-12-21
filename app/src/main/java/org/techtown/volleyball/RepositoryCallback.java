package org.techtown.volleyball;

public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
