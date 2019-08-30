package com.app.imagepicker.widget.scaleview.decoder;

/**
 * Interface for decoder (and region decoder) factories.
 * @param <T> the class of decoder that will be produced.
 *
 * Created by stefan on 2018/1/22.
 */
public interface DecoderFactory<T> {
  /**
   * Produce a new instance of a decoder with type {@link T}.
   * @return a new instance of your decoder.
   */
  T make() throws IllegalAccessException, InstantiationException;
}
