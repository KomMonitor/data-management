package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

public enum UpdateIntervalEnum {

	ARBITRARY("arbitrary"),	
	MONTHLY("monthly"),
	QUARTERLY("quarterly"),
	HALF_YEARLY("half yearly"),
	YEARLY("yearly");
	
	
	private String value;

    UpdateIntervalEnum(String value) {
      this.value = value;
    }

      /**
       *
       * @return
       */
      @Override
    public String toString() {
      return String.valueOf(value);
    }

      /**
       *
       * @param text
       * @return
       */

    public static UpdateIntervalEnum fromValue(String text) {
      for (UpdateIntervalEnum b : UpdateIntervalEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }


	
}
