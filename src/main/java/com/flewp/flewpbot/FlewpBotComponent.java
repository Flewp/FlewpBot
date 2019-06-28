package com.flewp.flewpbot;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {FlewpBotModule.class})
interface FlewpBotComponent {
    void inject(FlewpBot flewpBot);
}