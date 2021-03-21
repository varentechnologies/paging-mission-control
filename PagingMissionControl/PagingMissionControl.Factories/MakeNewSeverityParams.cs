using PagingMissionControl.Interfaces;
using PagingMissionControl.Params;

namespace PagingMissionControl.Factories
{
    /// <summary>
    /// Creates instances of objects that implement the
    /// <see
    ///     cref="T:PagingMissionControl.Interfaces.ISeverityParams" />
    /// interface, initializes them, and then returns references to them.
    /// </summary>
    public static class MakeNewSeverityParams
    {
        /// <summary>
        /// Initializes and returns a reference to an instance of an object that implements the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.ISeverityParams" />
        /// interface. The data provided is used to perform the initialization of the object's properties.
        /// </summary>
        /// <param name="redHighLimit">Maximum high-end tolerance for the particular physical quantity.</param>
        /// <param name="yellowHighLimit">Minimum high-end tolerance value for the particular physical quantity.</param>
        /// <param name="yellowLowLimit">Maximum low-end tolerance value for the particular physical quantity.</param>
        /// <param name="redLowLimit">Minimum low-end tolerance value for the particular physical quantity.</param>
        /// <param name="rawValue">Actual value of the physical quantity that is being reported.</param>
        /// <returns>
        /// Reference to an instance of an object that implements the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.ISeverityParams" />
        /// interface that is initialized with the data values provided.
        /// </returns>
        public static ISeverityParams FromData(decimal redHighLimit,
            decimal yellowHighLimit, decimal yellowLowLimit,
            decimal redLowLimit, decimal rawValue)
            => new SeverityParams
            {
                RedHighLimit = redHighLimit,
                YellowHighLimit = yellowHighLimit,
                YellowLowLimit = yellowLowLimit,
                RedLowLimit = redLowLimit,
                RawValue = rawValue
            };
    }
}