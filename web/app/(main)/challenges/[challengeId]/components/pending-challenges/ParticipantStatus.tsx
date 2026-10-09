import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { components } from "@/types/api";

type ParticipantStatusProps = {
  participants: components["schemas"]["ParticipantResponse"][];
};

export default function ParticipantStatus({
  participants,
}: ParticipantStatusProps) {
  return (
    <div>
      <Card>
        <CardHeader>
          <CardTitle>Participants</CardTitle>
        </CardHeader>
        <CardContent className="divide-y">
          {participants.map((participant) => (
            <div
              key={participant.id}
              className="flex justify-between py-2 my-0"
            >
              <div>{`${participant.username} (${participant.firstName} ${participant.lastName})`}</div>
              <div>{participant.ready ? "Ready" : "Not ready"}</div>
            </div>
          ))}
        </CardContent>
      </Card>
    </div>
  );
}
