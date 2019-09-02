package com.app.imagepicker.widget.scaleview.decoder;

import androidx.annotation.NonNull;

/**
 * Compatibility factory to instantiate decoders with empty public constructors.
 * 
 * @param <T> The base type of the decoder this factory will produce.
 *
 * Created by stefan on 2018/1/22.
 */
public class CompatDecoderFactory<T> implements DecoderFactory<T> {
  private Class<? extends T> clazz;

  public CompatDecoderFactory(@NonNull Class<? extends T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public T make() throws IllegalAccessException, InstantiationException {
    return clazz.newInstance();
  }
}
