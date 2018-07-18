package de.hsbo.kommonitor.datamanagement.api.impl.metadata;

public enum CreationTypeEnum {

	COMPUTED("computed"),
	INSERTED("inserted"),
	AGGREGATED("aggregated");
	
	
	private String value;

	CreationTypeEnum(String value) {
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

    public static CreationTypeEnum fromValue(String text) {
      for (CreationTypeEnum b : CreationTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }


	
}
