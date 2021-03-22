using System;

namespace PagingMissionControl.EventQueue.Interfaces
{
    /// <summary>Defines the publicly-exposed methods and properties of an interface for a pub/sub queue.</summary>
    public interface IEventQueue
    {
        /// <summary>Associates a handler method with an event having a specific unique identifier.
        /// <para />
        /// When the event having the identifier specified is later published to this event queue, all handler method(s) associated with the event are invoked.</summary>
        /// <param name="eventId">(Required.) Unique identifier that describes which event is being published.
        /// <para />
        /// May not be the Zero GUID.</param>
        /// <param name="handler">(Required.) Reference to an instance of a handler method whose signature matches that of the <see cref="T:System.Delegate" /> delegate type or its children.</param>
        /// <exception cref="T:System.ArgumentException">Thrown if the Zero GUID is passed for the <paramref name="eventId" /> parameter.</exception>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="handler"/>, is passed a <see langword="null" /> value.</exception>
        void MapEvent(Guid eventId, Delegate handler);

        /// <summary>Associates a handler method with an event having a specific unique identifier.
        /// <para />
        /// When the event having the identifier specified is later published to this event queue, all handler method(s) associated with the event are invoked.</summary>
        /// <param name="eventId">(Required.) Unique identifier that describes which event is being published.
        /// <para />
        /// May not be the Zero GUID.</param>
        /// <param name="handler">(Required.) Reference to an instance of a handler method whose signature matches that of the <see cref="T:System.EventHandler" /> delegate type.</param>
        /// <exception cref="T:System.ArgumentException">Thrown if the Zero GUID is passed for the <paramref name="eventId" /> parameter.</exception>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="handler"/>, is passed a <see langword="null" /> value.</exception>
        void MapEvent(Guid eventId, EventHandler handler);

        /// <summary>Associates a handler method with an event having a specific unique identifier.
        /// <para />
        /// When the event having the identifier specified is later published to this event queue, all handler method(s) associated with the event are invoked.</summary>
        /// <typeparam name="T">
        /// Type of data object, which must be of type
        /// <see
        ///     cref="T:System.EventArgs" />
        /// or one of its children, that carries data to be passed to the handler.
        /// </typeparam>
        /// <param name="eventId">(Required.) Unique identifier that describes which event is being published.
        /// <para />
        /// May not be the Zero GUID.</param>
        /// <param name="handler">(Required.) Reference to an instance of a handler method whose signature matches that of the <see cref="T:System.EventHandler" /> delegate type.</param>
        /// <exception cref="T:System.ArgumentException">Thrown if the Zero GUID is passed for the <paramref name="eventId" /> parameter.</exception>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="handler"/>, is passed a <see langword="null" /> value.</exception>
        void MapEvent<T>(Guid eventId, EventHandler<T> handler)
            where T : EventArgs;

        /// <summary>Publishes the event with the unique event identifier that specified by the <paramref name="eventId" /> parameter.
        /// <para />
        /// This overload publishes events that do not pass any arguments to their handler.</summary>
        /// <param name="eventId">(Required.) Unique identifier that describes which event is being published.
        /// <para />
        /// May not be the Zero GUID.</param>
        /// <remarks>Use this overload to publish events that do not need any arguments supplied to their handlers.</remarks>
        /// <exception cref="T:System.ArgumentException">Thrown if the Zero GUID is passed for the <paramref name="eventId" /> parameter.</exception>
        void PublishEvent(Guid eventId);

        /// <summary>Publishes the event with the event data</summary>
        /// <param name="eventId">(Required.) Unique identifier that describes which event is being published.
        /// <para />
        /// May not be the Zero GUID.
        /// <para />
        /// May not be the Zero GUID.</param>
        /// <param name="args">(Required.) One or more arguments to be passed to the invoked event handler.
        /// <para />
        /// The number, data type, and storage size of all arguments must be exactly the same as is specified in the event delegate's signature.</param>
        /// <exception cref="T:System.ArgumentException">Thrown if the Zero GUID is passed for the <paramref name="eventId" /> parameter.</exception>
        void PublishEvent(Guid eventId, params object[] args);
    }
}