package com.example.data.repository

import com.example.data.model.*

object EducationalContent {

  // Interactive Process: The Water Cycle
  val WaterCycleProcess = InteractiveProcess(
    id = "process_water_cycle",
    title = "The Global Hydrological Cycle",
    subtitle = "Thermodynamics, phase changes & planetary energy transport",
    category = SubjectCategory.SCIENCE,
    overview = "Earth's water cycle is a closed, massive thermodynamic heat engine driven by solar insolation. Continuous phase shifts between liquid, vapor, and solid move over 500,000 km³ of water each year, regulating planetary climate, ocean salinity, and life on land.",
    stages = listOf(
      ProcessStage(
        id = "stage_evaporation",
        stepNumber = 1,
        name = "Solar Evaporation",
        shortDescription = "Thermal energy breaks molecular hydrogen bonds in oceans and lakes.",
        detailedScience = "Solar photons penetrate the upper ocean boundary layer. Water molecules at the surface absorb kinetic thermal energy. When their kinetic energy exceeds the intermolecular hydrogen bonding threshold, they break away into the gas phase as invisible water vapor.",
        thermodynamicMetric = "ΔH_vap = +40.7 kJ/mol (Endothermic Phase Shift)",
        atmosphericImpact = "Accounts for ~86% of all atmospheric moisture on Earth.",
        visualHighlightArea = "ocean",
        keyCheckpointQuestion = QuizQuestion(
          id = "qc_evap_1",
          question = "What primary energy source powers 86% of planetary evaporation from oceans?",
          options = listOf("Solar radiation", "Geothermal vents", "Wind friction", "Lunar gravitational tides"),
          correctOptionIndex = 0,
          explanation = "Solar radiation supplies the necessary thermal kinetic energy (latent heat of vaporization) to liberate water molecules from liquid to gas."
        )
      ),
      ProcessStage(
        id = "stage_transpiration",
        stepNumber = 2,
        name = "Plant Transpiration",
        shortDescription = "Flora release moisture through microscopic leaf stomata.",
        detailedScience = "Through vascular xylem capillary action, plants draw water and dissolved minerals upward from soil to their canopies. During photosynthesis, microscopic leaf pores called stomata open to intake CO₂, simultaneously allowing water vapor to escape via diffusion.",
        thermodynamicMetric = "Latent heat cooling reduces canopy temperature by up to 5°C",
        atmosphericImpact = "The Amazon basin creates 'Flying Rivers' recycling moisture up to 6 times.",
        visualHighlightArea = "canopy",
        keyCheckpointQuestion = QuizQuestion(
          id = "qc_trans_1",
          question = "Through which microscopic botanical structures does plant transpiration occur?",
          options = listOf("Leaf stomata", "Chloroplast thylakoids", "Root tips", "Mitochondrial membranes"),
          correctOptionIndex = 0,
          explanation = "Stomata are microscopic apertures in plant epidermis controlled by guard cells that regulate gas exchange and moisture release."
        )
      ),
      ProcessStage(
        id = "stage_condensation",
        stepNumber = 3,
        name = "Condensation & Nucleation",
        shortDescription = "Rising humid air cools adiabatically, forming clouds on CCN aerosols.",
        detailedScience = "As warm, buoyant water vapor ascends through the troposphere, ambient atmospheric pressure drops. The expanding air cools at the dry/moist adiabatic lapse rate. Once the dew point is reached, water vapor aggregates onto microscopic airborne particles (cloud condensation nuclei) to form liquid micro-droplets.",
        thermodynamicMetric = "Releases latent heat (-40.7 kJ/mol), warming surrounding troposphere",
        atmosphericImpact = "Clouds increase planetary albedo, reflecting solar radiation back into space.",
        visualHighlightArea = "clouds",
        keyCheckpointQuestion = QuizQuestion(
          id = "qc_cond_1",
          question = "What happens to energy when water vapor condenses into cloud droplets?",
          options = listOf(
            "It releases latent heat to surrounding air",
            "It absorbs thermal energy from space",
            "Energy is permanently destroyed",
            "It cools the immediate air pocket"
          ),
          correctOptionIndex = 0,
          explanation = "Condensation is exothermic: the latent heat absorbed during evaporation is released back into the atmosphere, often fueling storm updrafts."
        )
      ),
      ProcessStage(
        id = "stage_transport",
        stepNumber = 4,
        name = "Atmospheric Advection",
        shortDescription = "Jet streams and prevailing winds convey clouds thousands of miles inland.",
        detailedScience = "Differences in solar heating across latitudes create planetary convection cells (Hadley, Ferrel, and Polar cells). Upper-level winds and the Coriolis effect propel condensed cloud masses from oceanic origins far across continental landmasses.",
        thermodynamicMetric = "Transports ~40 million kg of water vapor every second globally",
        atmosphericImpact = "Prevents deep continental interiors from desiccating into hyper-arid wasteland.",
        visualHighlightArea = "winds",
        keyCheckpointQuestion = QuizQuestion(
          id = "qc_transp_1",
          question = "What global mechanism transports cloud masses from oceans deep onto continents?",
          options = listOf(
            "Atmospheric wind advection & jet streams",
            "Oceanic gyre currents",
            "Subsurface tectonic plates",
            "Local capillary groundwater lift"
          ),
          correctOptionIndex = 0,
          explanation = "Atmospheric advection driven by planetary wind belts moves huge quantities of moisture-laden air across continents."
        )
      ),
      ProcessStage(
        id = "stage_precipitation",
        stepNumber = 5,
        name = "Precipitation & Orographic Lift",
        shortDescription = "Droplet coalescence overcomes updrafts, falling as rain or snow.",
        detailedScience = "Inside clouds, billions of micro-droplets collide and coalesce (or grow via the Bergeron-Findeisen ice crystal process). When individual droplets reach ~1 to 5 mm, their gravitational pull exceeds atmospheric updraft velocities, causing precipitation. When air hits mountain ranges, forced orographic lift triggers intense rainfall.",
        thermodynamicMetric = "Droplet terminal velocities reach 2.5 m/s to 9.0 m/s",
        atmosphericImpact = "Replenishes all global terrestrial freshwater reserves and topsoil.",
        visualHighlightArea = "mountains",
        keyCheckpointQuestion = QuizQuestion(
          id = "qc_precip_1",
          question = "What triggers rain or snow to actually fall from a cloud?",
          options = listOf(
            "Droplet gravitational weight exceeds cloud updrafts",
            "The cloud runs out of all electrical charge",
            "Sunlight stops reaching the top of the cloud",
            "Atmospheric pressure instantly drops to zero"
          ),
          correctOptionIndex = 0,
          explanation = "Precipitation occurs when droplet mass grows through coalescence until updrafts can no longer suspend them against gravity."
        )
      ),
      ProcessStage(
        id = "stage_runoff",
        stepNumber = 6,
        name = "Infiltration & Aquifer Storage",
        shortDescription = "Water percolates into deep aquifers or returns via river networks.",
        detailedScience = "Fallen precipitation follows gravity. A fraction runs across topography into streams, rivers, and estuaries (surface runoff). Another portion infiltrates soil porosity, percolating downward to recharge subterranean aquifers and maintain the water table, eventually discharging back into the oceans.",
        thermodynamicMetric = "Darcy's Law defines percolation rate: Q = -K * A * (dh/dl)",
        atmosphericImpact = "Closes the planetary circuit, transporting essential minerals and nutrients to marine ecosystems.",
        visualHighlightArea = "subsurface",
        keyCheckpointQuestion = QuizQuestion(
          id = "qc_run_1",
          question = "What term describes precipitation seeping downward through soil into groundwater aquifers?",
          options = listOf("Infiltration and percolation", "Sublimation", "Photolysis", "Desalination"),
          correctOptionIndex = 0,
          explanation = "Infiltration refers to water entering the topsoil, while percolation describes its downward passage through porous rock strata to subterranean aquifers."
        )
      )
    )
  )

  // Curated Micro-Lessons
  val Lessons: List<Lesson> = listOf(
    Lesson(
      id = "lesson_water_cycle",
      subjectCategory = SubjectCategory.SCIENCE,
      title = "The Water Cycle: Earth's Thermodynamic Engine",
      subtitle = "How solar energy and phase transitions circulate water across the biosphere",
      readTimeMinutes = 6,
      xpReward = 60,
      interactiveProcessId = "process_water_cycle",
      sections = listOf(
        LessonSection(
          title = "A Closed Planetary Thermodynamic Loop",
          content = "Earth's water cycle is one of the most magnificent closed thermodynamic loops in the solar system. Virtually every single droplet of water on Earth today has been recycled for over 4 billion years—passing through primordial oceans, prehistoric glaciers, and ancient living organisms.",
          keyFormulaOrConcept = "Conservation of Mass: dM_water/dt = Evaporation - Precipitation = 0 (Global Net)",
          didYouKnow = "The water molecules you drink today may have once fallen as rain on the Tyrannosaurus Rex 68 million years ago!"
        ),
        LessonSection(
          title = "Phase Changes & Latent Heat Flux",
          content = "Phase transitions between liquid, gas, and ice represent enormous transfers of energy. When water evaporates, it absorbs 2,260 Joules per gram of latent heat without increasing temperature. When that vapor condenses at higher altitudes, that exact latent heat is liberated into the upper atmosphere, serving as the primary fuel for cyclones and trade winds.",
          keyFormulaOrConcept = "Q = m * L_v (where L_v = 2.26 * 10^6 J/kg)",
          didYouKnow = "The release of latent heat in a single tropical thunderstorm can equal the energy of several nuclear weapons!"
        ),
        LessonSection(
          title = "Interactive Stage Exploration",
          content = "Explore each stage in our interactive simulation below! Adjust solar radiation, wind velocity, and observe how phase changes, cloud formation, and aquifer infiltration operate in real-time."
        )
      ),
      keyTakeaways = listOf(
        "Earth's water cycle is a closed mass system driven primarily by solar thermal insolation.",
        "Evaporation is endothermic (absorbs heat); condensation is exothermic (releases latent heat).",
        "Plants play a colossal role via transpiration—the Amazon creates atmospheric 'flying rivers'.",
        "Infiltration and groundwater storage sustain river base flows during dry seasons."
      ),
      quizQuestions = listOf(
        QuizQuestion(
          id = "q_wc_1",
          question = "What role does latent heat play when water vapor condenses into clouds?",
          options = listOf(
            "It releases thermal energy, warming the surrounding air",
            "It absorbs heat from space",
            "It has no thermodynamic effect",
            "It turns surrounding air into solid ice instantly"
          ),
          correctOptionIndex = 0,
          explanation = "Condensation is an exothermic phase change: water molecules release latent heat of vaporization into the atmosphere."
        ),
        QuizQuestion(
          id = "q_wc_2",
          question = "Which process describes botanical transpiration?",
          options = listOf(
            "Water vapor evaporating through leaf stomata",
            "Roots freezing subterranean aquifers",
            "Flowers absorbing carbon soot",
            "Bark decomposing into soil"
          ),
          correctOptionIndex = 0,
          explanation = "Transpiration is the evaporation of water from plant aerial parts, primarily through microscopic stomata on leaf surfaces."
        ),
        QuizQuestion(
          id = "q_wc_3",
          question = "What is the global net change in total water mass over time on Earth?",
          options = listOf(
            "Effectively zero (conservation of mass in a closed system)",
            "Losing 50% every century",
            "Doubling every 1,000 years",
            "Constantly leaking entirely into lunar orbit"
          ),
          correctOptionIndex = 0,
          explanation = "Apart from negligible atmospheric escape into space and occasional cometary dust, Earth's water mass is strictly conserved."
        )
      )
    ),
    Lesson(
      id = "lesson_neural_networks",
      subjectCategory = SubjectCategory.TECHNOLOGY,
      title = "How Artificial Neural Networks Learn",
      subtitle = "From artificial neurons and weights to loss gradients and backpropagation",
      readTimeMinutes = 5,
      xpReward = 50,
      sections = listOf(
        LessonSection(
          title = "The Artificial Neuron (Perceptron)",
          content = "An artificial neuron takes multiple numerical inputs (x_i), multiplies each by an adjustable weight (w_i), sums them with a bias (b), and passes the result through a non-linear activation function (like ReLU or GELU). This non-linearity allows networks to approximate any mathematical function.",
          keyFormulaOrConcept = "y = σ( ∑ (w_i * x_i) + b )",
          didYouKnow = "Without non-linear activation functions, a 100-layer neural network collapses mathematically into a single linear equation!"
        ),
        LessonSection(
          title = "Gradient Descent & The Calculus of Learning",
          content = "Learning occurs by measuring the prediction error with a Loss Function, then calculating the partial derivative of error with respect to every weight in the network. Backpropagation uses the Calculus Chain Rule to propagate this error backwards.",
          keyFormulaOrConcept = "w_new = w_old - η * (∂Loss / ∂w)",
          didYouKnow = "Modern frontier LLMs adjust hundreds of billions of weights simultaneously during every single gradient step."
        )
      ),
      keyTakeaways = listOf(
        "Neurons perform weighted sums followed by non-linear activations.",
        "Non-linearities (like ReLU) enable networks to model complex real-world boundaries.",
        "Backpropagation applies the calculus chain rule to calculate weight gradients.",
        "Gradient descent iteratively updates weights to minimize loss."
      ),
      quizQuestions = listOf(
        QuizQuestion(
          id = "q_nn_1",
          question = "Why are non-linear activation functions essential in deep neural networks?",
          options = listOf(
            "They allow networks to learn non-linear patterns and boundaries",
            "They reduce RAM usage to zero",
            "They turn floating point numbers into integers",
            "They encrypt the model parameters"
          ),
          correctOptionIndex = 0,
          explanation = "Without non-linear activations, stacking multiple layers produces only a linear combination, restricting capacity to simple linear regression."
        ),
        QuizQuestion(
          id = "q_nn_2",
          question = "What mathematical theorem underpins backpropagation?",
          options = listOf(
            "The Chain Rule of Calculus",
            "Pythagorean Theorem",
            "Fermat's Last Theorem",
            "Goldbach's Conjecture"
          ),
          correctOptionIndex = 0,
          explanation = "The Chain Rule allows efficient calculation of composite function derivatives from output layer backwards to input layer."
        )
      )
    ),
    Lesson(
      id = "lesson_eulers_identity",
      subjectCategory = SubjectCategory.MATHEMATICS,
      title = "Euler's Identity: The Most Beautiful Equation",
      subtitle = "Uniting five fundamental mathematical constants in a single elegant truth",
      readTimeMinutes = 4,
      xpReward = 50,
      sections = listOf(
        LessonSection(
          title = "Five Constants, One Equation",
          content = "Euler's Identity connects: e (base of natural logarithms), i (imaginary unit √-1), π (ratio of circle circumference to diameter), 1 (multiplicative identity), and 0 (additive identity). It reveals profound geometric harmony between trigonometry, exponentials, and the complex plane.",
          keyFormulaOrConcept = "e^(i * π) + 1 = 0",
          didYouKnow = "Physicist Richard Feynman called Euler's formula 'our jewel' and the most remarkable formula in mathematics."
        ),
        LessonSection(
          title = "Rotation in the Complex Plane",
          content = "Multiplying by e^(i*θ) corresponds to a counter-clockwise rotation by angle θ radians on the complex unit circle. When θ = π (180 degrees), the vector lands squarely on -1 on the real axis! Therefore: e^(i*π) = -1, which rearranges to e^(i*π) + 1 = 0.",
          keyFormulaOrConcept = "e^(i * θ) = cos(θ) + i * sin(θ)"
        )
      ),
      keyTakeaways = listOf(
        "Euler's formula e^(iθ) = cos(θ) + i*sin(θ) links exponentials to circular trigonometry.",
        "Plugging θ = π produces e^(iπ) = -1.",
        "Connects arithmetic, geometry, calculus, and complex analysis."
      ),
      quizQuestions = listOf(
        QuizQuestion(
          id = "q_eu_1",
          question = "What geometric operation does multiplying by e^(i*θ) perform in the complex plane?",
          options = listOf(
            "A rotation by θ radians around the origin",
            "A scaling by a factor of 10",
            "A translation along the x-axis only",
            "A reflection across the hyperbola"
          ),
          correctOptionIndex = 0,
          explanation = "In the complex plane, e^(i*θ) lies on the unit circle at angle θ, acting as a pure rotational operator."
        )
      )
    ),
    Lesson(
      id = "lesson_cognitive_biases",
      subjectCategory = SubjectCategory.PHILOSOPHY,
      title = "Cognitive Biases: Flaws in Human Intuition",
      subtitle = "How our evolutionary heuristics lead to systematic cognitive errors",
      readTimeMinutes = 5,
      xpReward = 50,
      sections = listOf(
        LessonSection(
          title = "Confirmation Bias: The Echo Chamber Trap",
          content = "Confirmation bias is our tendency to search for, interpret, and recall information in a way that confirms prior beliefs, while dismissing contrary data. Francis Bacon noted in 1620 that the human mind, once it adopts an opinion, draws all other things to support it.",
          didYouKnow = "In scientific inquiry, the Falsification Principle (Karl Popper) was established specifically to defeat confirmation bias."
        ),
        LessonSection(
          title = "Availability Heuristic & Probability Blindness",
          content = "Humans judge the likelihood of events based on how easily examples spring to mind. Plane crashes or shark attacks feel common because they are dramatic and vivid in news headlines, whereas heart disease or falls are statistically hundreds of times more perilous.",
          keyFormulaOrConcept = "Rational Thinking = Base Rates × Bayesian Evidence Updating"
        )
      ),
      keyTakeaways = listOf(
        "Cognitive biases are systematic deviations from rational judgment.",
        "Confirmation bias leads people to dismiss disconfirming evidence.",
        "Availability heuristic causes emotional overestimation of vivid, rare events.",
        "Deliberate Bayesian updating and Socratic questioning help mitigate these blind spots."
      ),
      quizQuestions = listOf(
        QuizQuestion(
          id = "q_cb_1",
          question = "What is confirmation bias?",
          options = listOf(
            "The tendency to only accept data that confirms pre-existing beliefs",
            "The ability to confirm mathematical proofs instantly",
            "The bias of computers during binary hashing",
            "A preference for loud noises in speech"
          ),
          correctOptionIndex = 0,
          explanation = "Confirmation bias filters incoming data so that only evidence supporting one's hypothesis is remembered or validated."
        )
      )
    ),
    Lesson(
      id = "lesson_space_race",
      subjectCategory = SubjectCategory.HISTORY,
      title = "The Space Race: From Sputnik to Apollo",
      subtitle = "How geopolitical rivalry propelled humanity beyond Earth's gravitational cradle",
      readTimeMinutes = 5,
      xpReward = 50,
      sections = listOf(
        LessonSection(
          title = "The Satellite That Shook the World",
          content = "On October 4, 1957, the Soviet Union launched Sputnik 1—a 83.6-kg polished metal sphere emitting repetitive beeps. It orbited Earth every 96 minutes, initiating the modern space age and proving orbital rocketry feasibility.",
          didYouKnow = "The Apollo Guidance Computer (AGC) operated at 2.048 MHz and had only 4 KB of RAM—less power than a modern musical greeting card!"
        ),
        LessonSection(
          title = "Saturn V & The Lunar Landings",
          content = "Engineered under Wernher von Braun, the Saturn V rocket stood 111 meters tall and produced 34.5 million Newtons of thrust at liftoff. In July 1969, Apollo 11 landed Neil Armstrong and Buzz Aldrin on the Sea of Tranquility."
        )
      ),
      keyTakeaways = listOf(
        "Sputnik 1 (1957) ignited competitive technological leaps across aerospace and computing.",
        "Yuri Gagarin became the first human in space in April 1961.",
        "Apollo 11 landed humans on the Moon in July 1969 via the Saturn V rocket.",
        "The technological dividends include CMOS sensors, GPS, and advanced materials."
      ),
      quizQuestions = listOf(
        QuizQuestion(
          id = "q_sr_1",
          question = "Which mission was the first to successfully land humans on the Moon?",
          options = listOf("Apollo 11", "Apollo 13", "Vostok 1", "Gemini 4"),
          correctOptionIndex = 0,
          explanation = "Apollo 11 landed astronauts Neil Armstrong and Buzz Aldrin on the lunar surface on July 20, 1969."
        )
      )
    )
  )

  // Active Recall Flashcards
  val Flashcards: List<FlashcardItem> = listOf(
    FlashcardItem(
      id = "fc_1",
      category = SubjectCategory.SCIENCE,
      question = "What is the Latent Heat of Vaporization for water?",
      answer = "Approximately 2,260 J/g (or 40.7 kJ/mol)",
      deepNote = "It is the energy required to transform 1g of liquid water into vapor without changing its temperature."
    ),
    FlashcardItem(
      id = "fc_2",
      category = SubjectCategory.SCIENCE,
      question = "What are Cloud Condensation Nuclei (CCN)?",
      answer = "Microscopic airborne aerosols (salt, dust, pollen) upon which water vapor condenses to form cloud droplets.",
      deepNote = "Without CCN, relative humidity would have to exceed 300% for homogenous condensation to initiate."
    ),
    FlashcardItem(
      id = "fc_3",
      category = SubjectCategory.TECHNOLOGY,
      question = "What is Big-O notation?",
      answer = "A mathematical notation that describes the limiting behavior of an algorithm as the input size (n) grows towards infinity.",
      deepNote = "Common classes: O(1) constant, O(log n) logarithmic, O(n) linear, O(n log n) linearithmic, O(n²) quadratic."
    ),
    FlashcardItem(
      id = "fc_4",
      category = SubjectCategory.TECHNOLOGY,
      question = "What is the primary role of a non-linear activation function in deep learning?",
      answer = "It introduces non-linearities, allowing neural networks to model non-linear real-world relationships.",
      deepNote = "Common examples: ReLU (Rectified Linear Unit), Leaky ReLU, GeLU, and Sigmoid."
    ),
    FlashcardItem(
      id = "fc_5",
      category = SubjectCategory.MATHEMATICS,
      question = "What does Euler's formula state for any real number x?",
      answer = "e^(i*x) = cos(x) + i * sin(x)",
      deepNote = "Discovered by Leonhard Euler in 1748, it unifies circular trigonometry with complex exponential analysis."
    ),
    FlashcardItem(
      id = "fc_6",
      category = SubjectCategory.PHILOSOPHY,
      question = "What is Popper's Falsification Principle?",
      answer = "For a scientific hypothesis to be valid, it must be inherently testable and capable of being proven false.",
      deepNote = "Formulated by Karl Popper to delineate genuine science from pseudo-science."
    )
  )

  // Reference Formulas and Cheat Sheets
  val Formulas: List<FormulaItem> = listOf(
    FormulaItem(
      id = "form_wc_heat",
      title = "Latent Heat of Phase Transition",
      formula = "Q = m * L_v",
      category = SubjectCategory.SCIENCE,
      explanation = "Calculates the thermal energy absorbed during evaporation or released during condensation, where m is mass and L_v is the specific latent heat.",
      application = "Used by meteorologists to model cyclone hurricane power from warm ocean waters."
    ),
    FormulaItem(
      id = "form_darcy",
      title = "Darcy's Law of Groundwater Infiltration",
      formula = "Q = -K * A * (dh / dl)",
      category = SubjectCategory.SCIENCE,
      explanation = "Defines the volumetric discharge of groundwater through a porous aquifer medium (hydraulic conductivity K, area A, hydraulic gradient dh/dl).",
      application = "Critical for calculating municipal aquifer recharge rates and well yields."
    ),
    FormulaItem(
      id = "form_euler",
      title = "Euler's Identity",
      formula = "e^(i * π) + 1 = 0",
      category = SubjectCategory.MATHEMATICS,
      explanation = "Unites the five most important mathematical constants (e, i, π, 1, 0) into one equation.",
      application = "Foundation of electrical engineering AC circuits, quantum wavefunctions, and Fourier analysis."
    ),
    FormulaItem(
      id = "form_bayes",
      title = "Bayes' Theorem",
      formula = "P(A|B) = [P(B|A) * P(A)] / P(B)",
      category = SubjectCategory.MATHEMATICS,
      explanation = "Updates the probability of a hypothesis (A) based on observed prior evidence (B).",
      application = "Applied in medical diagnosis, spam filtering, and rational decision theory."
    ),
    FormulaItem(
      id = "form_grad_desc",
      title = "Gradient Descent Optimization",
      formula = "θ_{t+1} = θ_t - η * ∇J(θ_t)",
      category = SubjectCategory.TECHNOLOGY,
      explanation = "Iteratively updates neural network parameters θ in the opposite direction of the gradient of the objective function J.",
      application = "The core engine powering machine learning model training."
    )
  )
}
