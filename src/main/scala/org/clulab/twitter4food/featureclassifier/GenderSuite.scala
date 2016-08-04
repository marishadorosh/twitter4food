package org.clulab.twitter4food.featureclassifier

import com.typesafe.config.ConfigFactory
import org.clulab.twitter4food.util.FileUtils
import org.slf4j.LoggerFactory

object GenderSuite {
  def main(args: Array[String]) {
    val logger = LoggerFactory.getLogger(this.getClass)
    val config = ConfigFactory.load

    logger.info("Loading training accounts...")
    val train = FileUtils.load(config.getString("classifiers.gender.trainingData"))
    logger.info("Loading dev accounts...")
    val dev = FileUtils.load(config.getString("classifiers.gender.devData"))

    logger.info("Loading follower accounts...")
    val followers = ClassifierImpl.loadFollowers(train.keys.toSeq ++ dev.keys.toSeq)

    val gc = new GenderClassifier(
      useUnigrams = true,
      useBigrams = true,
      useTopics = true,
      useDictionaries = true,
      useEmbeddings = true,
      useCosineSim = true,
      useFollowers = true,
      useFollowees = true,
      useRace = true,
      datumScaling = true
    )

    gc.featureSelectionIncremental(train ++ dev, followers)
  }
}