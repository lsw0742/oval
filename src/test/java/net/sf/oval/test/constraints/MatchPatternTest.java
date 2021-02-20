/*
 * Copyright 2005-2021 by Sebastian Thomschke and contributors.
 * SPDX-License-Identifier: EPL-2.0
 */
package net.sf.oval.test.constraints;

import static org.assertj.core.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.Test;

import net.sf.oval.constraint.MatchPatternCheck;

/**
 * @author Sebastian Thomschke
 */
public class MatchPatternTest extends AbstractContraintsTest {

   @Test
   public void testMatchPattern() {
      final MatchPatternCheck check = new MatchPatternCheck();
      super.testCheck(check);

      check.setMatchAll(true);
      check.setPattern("\\d*", 0);
      assertThat(check.isSatisfied(null, null, null)).isTrue();
      assertThat(check.isSatisfied(null, "", null)).isTrue();
      assertThat(check.isSatisfied(null, "1234", null)).isTrue();
      assertThat(check.isSatisfied(null, "12.34", null)).isFalse();
      assertThat(check.isSatisfied(null, "12,34", null)).isFalse();
      assertThat(check.isSatisfied(null, "foo", null)).isFalse();

      check.setPatterns(Pattern.compile("[1234]*", 0), Pattern.compile("[1256]*", 0));
      assertThat(check.isSatisfied(null, null, null)).isTrue();
      assertThat(check.isSatisfied(null, "", null)).isTrue();
      assertThat(check.isSatisfied(null, "1212", null)).isTrue();
      assertThat(check.isSatisfied(null, "1234", null)).isFalse();
      assertThat(check.isSatisfied(null, "1256", null)).isFalse();
      assertThat(check.isSatisfied(null, "34", null)).isFalse();
      assertThat(check.isSatisfied(null, "56", null)).isFalse();

      check.setMatchAll(false);
      assertThat(check.isSatisfied(null, "1212", null)).isTrue();
      assertThat(check.isSatisfied(null, "1234", null)).isTrue();
      assertThat(check.isSatisfied(null, "1256", null)).isTrue();
      assertThat(check.isSatisfied(null, "34", null)).isTrue();
      assertThat(check.isSatisfied(null, "56", null)).isTrue();
   }
}
