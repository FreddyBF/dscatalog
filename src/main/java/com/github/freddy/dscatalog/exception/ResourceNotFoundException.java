package com.github.freddy.dscatalog.exception;

public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}